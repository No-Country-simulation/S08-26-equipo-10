package com.fieldflow.execution.application;

import com.fieldflow.conformity.domain.Evidence;
import com.fieldflow.conformity.persistence.EvidenceRepository;
import com.fieldflow.execution.api.dto.CreateInterventionRequest;
import com.fieldflow.execution.api.dto.InterventionDetailResponse;
import com.fieldflow.execution.application.mapper.InterventionMapper;
import com.fieldflow.execution.domain.*;
import com.fieldflow.execution.persistence.*;
import com.fieldflow.planning.application.SchedulingService;
import com.fieldflow.shared.exception.ApiException;
import com.fieldflow.workorders.domain.WorkOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

	private final SchedulingService schedulingService;

	public InterventionServiceImpl(InterventionRepository repository,
	                               FailureRepository failureRepository,
	                               RepairRepository repairRepository,
	                               InterventionComponentRepository componentRepository,
	                               ChecklistItemAnswerRepository answerRepository,
	                               TechnicalNoteRepository technicalNoteRepository,
	                               EvidenceRepository evidenceRepository,
	                               SchedulingService schedulingService) {
		this.repository = repository;
		this.failureRepository = failureRepository;
		this.repairRepository = repairRepository;
		this.componentRepository = componentRepository;
		this.answerRepository = answerRepository;
		this.technicalNoteRepository = technicalNoteRepository;
		this.evidenceRepository = evidenceRepository;
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
}
