package com.fieldflow.execution.application;

import com.fieldflow.conformity.domain.Evidence;
import com.fieldflow.conformity.persistence.EvidenceRepository;
import com.fieldflow.execution.api.dto.CreateInterventionRequest;
import com.fieldflow.execution.api.dto.InterventionDetailResponse;
import com.fieldflow.execution.api.dto.InterventionReportRequest;
import com.fieldflow.execution.api.dto.InterventionReportRequest.ChecklistAnswerRequest;
import com.fieldflow.execution.api.dto.InterventionReportRequest.EvidenceRequest;
import com.fieldflow.execution.api.dto.InterventionReportResponse;
import com.fieldflow.execution.application.mapper.InterventionMapper;
import com.fieldflow.execution.domain.*;
import com.fieldflow.execution.persistence.*;
import com.fieldflow.planning.application.SchedulingService;
import com.fieldflow.shared.exception.ApiException;
import com.fieldflow.storage.EvidenceUpload;
import com.fieldflow.storage.EvidenceUploadRepository;
import com.fieldflow.workorders.domain.WorkOrder;
import com.fieldflow.workorders.domain.WorkOrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InterventionServiceImpl implements InterventionService {

	private final InterventionRepository repository;

	private final FailureRepository failureRepository;
	private final RepairRepository repairRepository;
	private final InterventionComponentRepository componentRepository;
	private final ChecklistItemAnswerRepository answerRepository;
	private final TechnicalNoteRepository technicalNoteRepository;
	private final EvidenceRepository evidenceRepository;
	private final ChecklistItemRepository checklistItemRepository;

	private final EvidenceUploadRepository evidenceUploadRepository;

	private final SchedulingService schedulingService;

	public InterventionServiceImpl(InterventionRepository repository,
	                               FailureRepository failureRepository,
	                               RepairRepository repairRepository,
	                               InterventionComponentRepository componentRepository,
	                               ChecklistItemAnswerRepository answerRepository,
	                               TechnicalNoteRepository technicalNoteRepository,
	                               EvidenceRepository evidenceRepository,
	                               ChecklistItemRepository checklistItemRepository,
	                               EvidenceUploadRepository evidenceUploadRepository,
	                               SchedulingService schedulingService) {
		this.repository = repository;
		this.failureRepository = failureRepository;
		this.repairRepository = repairRepository;
		this.componentRepository = componentRepository;
		this.answerRepository = answerRepository;
		this.technicalNoteRepository = technicalNoteRepository;
		this.evidenceRepository = evidenceRepository;
		this.checklistItemRepository = checklistItemRepository;
		this.evidenceUploadRepository = evidenceUploadRepository;
		this.schedulingService = schedulingService;
	}

	@Override
	@Transactional(readOnly = true)
	public List<InterventionDetailResponse> getInterventionsByWorkOrderId(UUID workOrderId) {
		List<Intervention> interventions = repository.findAllByWorkOrderId(workOrderId);
		List<UUID> interventionIds = interventions.stream().map(Intervention::getId).toList();

		Map<UUID, InterventionDetails> detailsByInterventionId = getDetailsByInterventionIds(interventionIds);

		return interventions.stream()
				.map(intervention -> {
					InterventionDetails details = detailsByInterventionId.getOrDefault(intervention.getId(),
							InterventionDetails.empty());
					return InterventionMapper.toDetailResponse(intervention, details);
				}).toList();
	}

	@Override
	@Transactional
	public Intervention recordInterventionStart(WorkOrder workOrder, CreateInterventionRequest request) {
		var technician = schedulingService.getAssignedTechnicianForWorkOrder(workOrder.getId());

		if (!technician.getId().equals(request.technicianId())) {
			throw ApiException.technicianNotAssigned("""
					El técnico indicado no es el técnico asignado a esta Orden de Trabajo;
					no puede iniciar la intervención.
					""");
		}

		if (!workOrder.getStatus().canStartIntervention()) {
			throw ApiException.invalidStatusTransition("""
					No se puede iniciar la intervención.
					La Orden de Trabajo debe encontrarse en estado ASSIGNED, EN_ROUTE o IN_PROGRESS.
					""");
		}

		var intervention = new Intervention(
				workOrder,
				technician,
				request.startedAt(),
				InterventionStatus.IN_PROGRESS
		);

		return repository.save(intervention);
	}

	@Override
	@Transactional(readOnly = true)
	public void validateUploadAllowed(UUID interventionId) {
		Intervention intervention = repository.findByIdWithWorkOrder(interventionId)
				.orElseThrow(() -> ApiException.notFound(
						"No existe una intervención asociada al ID " + interventionId + "."
				));

		if (intervention.getStatus() != InterventionStatus.IN_PROGRESS
				|| intervention.getEndedAt() != null
				|| intervention.getWorkOrder().getStatus() != WorkOrderStatus.IN_PROGRESS) {
			throw ApiException.resourceStateConflict(
					"La intervención y su Orden de Trabajo deben estar en `IN_PROGRESS` para subir imágenes."
			);
		}
	}

	@Override
	@Transactional
	public InterventionReportResponse submitReport(UUID interventionId, InterventionReportRequest request) {
		Intervention intervention = repository.findForReportUpdate(interventionId)
				.orElseThrow(() -> ApiException.notFound(
						"No existe una intervención asociada al ID " + interventionId + "."
				));

		if (request.endedAt().isBefore(intervention.getStartedAt())) {
			throw ApiException.badRequest(
					"La fecha de finalización no puede ser anterior a la fecha de inicio."
			);
		}

		if (intervention.getStatus() != InterventionStatus.IN_PROGRESS
				|| intervention.getEndedAt() != null
				|| intervention.getWorkOrder().getStatus() != WorkOrderStatus.IN_PROGRESS) {
			throw ApiException.resourceStateConflict(
					"La intervención y su Orden de Trabajo deben estar en `IN_PROGRESS` para enviar el reporte."
			);
		}

		validateUniqueEvidenceReferences(request.evidence());
		Map<UUID, ChecklistItem> itemsById = loadValidChecklistItems(intervention, request.checklistAnswers());
		List<EvidenceUpload> evidenceUploads = loadEvidenceUploads(interventionId, request.evidence());
		validateAvailableUploads(evidenceUploads);

		List<ChecklistItemAnswer> answers = request.checklistAnswers().stream()
				.map(answer -> new ChecklistItemAnswer(
						intervention,
						itemsById.get(answer.checklistItemId()),
						answer.value(),
						answer.observation()
				)).toList();

		List<Failure> failures = request.failures().stream()
				.map(failure -> new Failure(intervention, failure.description()))
				.toList();

		List<Repair> repairs = request.repairs().stream()
				.map(repair -> new Repair(intervention, repair.description()))
				.toList();

		List<InterventionComponent> components = request.components().stream()
				.map(component -> new InterventionComponent(
						intervention,
						component.componentName(),
						component.action(),
						component.description()
				)).toList();

		List<TechnicalNote> technicalNotes = request.technicalNotes().stream()
				.map(technicalNote -> new TechnicalNote(
						intervention,
						technicalNote.content()
				)).toList();

		failureRepository.saveAll(failures);
		answerRepository.saveAll(answers);
		repairRepository.saveAll(repairs);
		componentRepository.saveAll(components);
		technicalNoteRepository.saveAll(technicalNotes);

		saveReportEvidence(intervention, request.evidence(), evidenceUploads);

		intervention.markAsPendingCustomerConfirmation(request.result(), request.observations(), request.endedAt());
		intervention.getWorkOrder().setStatus(WorkOrderStatus.PENDING_CUSTOMER_CONFIRMATION);

		return new InterventionReportResponse(
				intervention.getId(),
				intervention.getStatus(),
				intervention.getWorkOrder().getStatus()
		);
	}

	private Map<UUID, InterventionDetails> getDetailsByInterventionIds(Collection<UUID> interventionIds) {
		if (interventionIds.isEmpty()) {
			return Map.of();
		}

		Map<UUID, List<Failure>> failuresByInterventionId =
				failureRepository.findAllByInterventionIds(interventionIds)
						.stream()
						.collect(Collectors.groupingBy(failure -> failure.getIntervention().getId()));

		Map<UUID, List<Repair>> repairsByInterventionId =
				repairRepository.findAllByInterventionIds(interventionIds)
						.stream()
						.collect(Collectors.groupingBy(repair -> repair.getIntervention().getId()));

		Map<UUID, List<InterventionComponent>> componentsByInterventionId =
				componentRepository.findAllByInterventionIds(interventionIds)
						.stream()
						.collect(Collectors.groupingBy(component -> component.getIntervention().getId()));

		Map<UUID, List<ChecklistItemAnswer>> checklistResponsesByInterventionId =
				answerRepository.findAllWithItemByInterventionIds(interventionIds)
						.stream()
						.collect(Collectors.groupingBy(response -> response.getIntervention().getId()));

		Map<UUID, List<TechnicalNote>> technicalNotesByInterventionId =
				technicalNoteRepository.findAllByInterventionIds(interventionIds)
						.stream()
						.collect(Collectors.groupingBy(note -> note.getIntervention().getId()));

		Map<UUID, List<Evidence>> evidenceByInterventionId =
				evidenceRepository.findAllByInterventionIds(interventionIds)
						.stream()
						.collect(Collectors.groupingBy(evidence -> evidence.getIntervention().getId()));

		return interventionIds.stream()
				.collect(Collectors.toMap(Function.identity(), interventionId -> new InterventionDetails(
						failuresByInterventionId.getOrDefault(interventionId, List.of()),
						repairsByInterventionId.getOrDefault(interventionId, List.of()),
						componentsByInterventionId.getOrDefault(interventionId, List.of()),
						checklistResponsesByInterventionId.getOrDefault(interventionId, List.of()),
						technicalNotesByInterventionId.getOrDefault(interventionId, List.of()),
						evidenceByInterventionId.getOrDefault(interventionId, List.of())
				)));
	}

	private Map<UUID, ChecklistItem> loadValidChecklistItems(Intervention intervention,
	                                                         List<ChecklistAnswerRequest> answers) {
		if (answers.isEmpty()) {
			return Map.of();
		}

		// en la request pueden llegar respuestas duplicadas; esta función las descarta
		Set<UUID> itemIds = answers.stream()
				.map(ChecklistAnswerRequest::checklistItemId)
				.collect(Collectors.toSet());

		// se evalúa si existieron duplicados
		if (itemIds.size() != answers.size()) {
			throw ApiException.resourceStateConflict(
					"Se intenta responder un Item que ya cuenta con una respuesta en esta intervención."
			);
		}

		// se buscan los ítems de la checklist que corresponden a la orden de trabajo actual
		List<ChecklistItem> validItems = checklistItemRepository
				.findAllForWorkOrder(intervention.getWorkOrder().getId(), itemIds);

		// en este punto puede haber items que no existen o no pertenecen a la orden de trabajo actual
		if (validItems.size() != itemIds.size()) {
			throw ApiException.badRequest(
					"Algún ítem no existe o no pertenece a la checklist de la Orden de Trabajo actual."
			);
		}

		// se verifica que no existan respuestas previamente registradas para estos ítems
		if (answerRepository.countExistingAnswers(intervention.getId(), itemIds) > 0) {
			throw ApiException.resourceStateConflict(
					"La intervención ya tiene una respuesta registrada para uno de los ítems de checklist."
			);
		}

		return validItems.stream().collect(Collectors.toMap(ChecklistItem::getId, Function.identity()));
	}

	private void validateUniqueEvidenceReferences(List<EvidenceRequest> evidence) {
		Set<String> references = new HashSet<>();
		for (var item : evidence) {
			if (!references.add(item.reference())) {
				throw ApiException.resourceStateConflict(
						"La misma referencia de evidencia aparece más de una vez en el reporte."
				);
			}
		}
	}

	private List<EvidenceUpload> loadEvidenceUploads(UUID interventionId, List<EvidenceRequest> evidence) {
		if (evidence.isEmpty()) {
			return List.of();
		}

		List<String> references = evidence.stream()
				.map(EvidenceRequest::reference)
				.toList();

		List<EvidenceUpload> uploads = evidenceUploadRepository.findAllForAttachment(interventionId, references);

		if (uploads.size() != references.size()) {
			throw ApiException.badRequest(
					"Una o más referencias no existen o no pertenecen a esta intervención."
			);
		}

		return uploads;
	}

	private void validateAvailableUploads(List<EvidenceUpload> uploads) {
		for (EvidenceUpload upload : uploads) {
			if (upload.getUploadedAt() == null) {
				throw ApiException.resourceStateConflict(
						"Una de las imágenes todavía no terminó de subirse.");
			}

			if (upload.getAttachedAt() != null || upload.getEvidenceId() != null) {
				throw ApiException.resourceStateConflict(
						"Una de las imágenes ya está asociada a un reporte.");
			}
		}
	}

	private void saveReportEvidence(Intervention intervention,
	                                List<EvidenceRequest> requested,
	                                List<EvidenceUpload> uploads) {
		if (requested.isEmpty()) {
			return;
		}

		Map<String, EvidenceUpload> uploadsByReference = uploads.stream()
				.collect(Collectors.toMap(EvidenceUpload::getReference, Function.identity()));

		List<Evidence> saved = evidenceRepository.saveAllAndFlush(
				requested.stream()
						.map(item -> new Evidence(
								intervention,
								item.type(),
								item.reference(),
								item.description()))
						.toList());

		OffsetDateTime attachedAt = OffsetDateTime.now(ZoneOffset.UTC);

		for (Evidence evidence : saved) {
			uploadsByReference.get(evidence.getReference()).attachTo(evidence.getId(), attachedAt);
		}
	}
}
