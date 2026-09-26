package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.*;
import com.fieldflow.planning.application.mapper.AssignmentMapper;
import com.fieldflow.planning.application.mapper.TechnicianMapper;
import com.fieldflow.planning.domain.Assignment;
import com.fieldflow.planning.domain.Technician;
import com.fieldflow.planning.domain.TechnicianAvailability;
import com.fieldflow.planning.persistence.AssignmentRepository;
import com.fieldflow.planning.persistence.TechnicianAvailabilityRepository;
import com.fieldflow.planning.persistence.TechnicianRepository;
import com.fieldflow.shared.exception.ApiErrorType;
import com.fieldflow.shared.exception.ApiException;
import com.fieldflow.workorders.domain.WorkOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SchedulingServiceImp implements SchedulingService {

	private final Duration MIN_AVAILABILITY_DURATION = Duration.ofMinutes(240);

	private final AssignmentRepository assignmentRepository;
	private final TechnicianAvailabilityRepository technicianAvailabilityRepository;
	private final TechnicianRepository technicianRepository;

	public SchedulingServiceImp(AssignmentRepository assignmentRepository,
	                            TechnicianAvailabilityRepository technicianAvailabilityRepository,
	                            TechnicianRepository technicianRepository) {
		this.assignmentRepository = assignmentRepository;
		this.technicianAvailabilityRepository = technicianAvailabilityRepository;
		this.technicianRepository = technicianRepository;
	}


	@Override
	@Transactional
	public Assignment createAssignment(WorkOrder workOrder, AssignmentRequest request) {
		var technician = technicianRepository.findById(request.technicianId())
				.orElseThrow(() -> ApiException.notFound("No existe técnico asociado al ID " + request.technicianId()));

		// validates technician availability
		boolean available = technicianAvailabilityRepository.countContainingAvailability(technician.getId(),
				request.plannedStartAt(), request.plannedEndAt()) > 0;

		if (!available) {
			throw ApiException.conflict(
					"El técnico no se encuentra disponible durante el intervalo solicitado.",
					ApiErrorType.TECHNICIAN_NOT_AVAILABLE
			);
		}

		// validates overlapping assignments
		boolean overlaps = assignmentRepository.countOverlappingAssignments(technician.getId(),
				workOrder.getId(), request.plannedStartAt(), request.plannedEndAt()) > 0;

		if (overlaps) {
			throw ApiException.conflict(
					"El técnico ya posee una asignación que se solapa con el intervalo solicitado.",
					ApiErrorType.SCHEDULE_OVERLAP
			);
		}

		Assignment assignment = assignmentRepository.findByWorkOrderId(workOrder.getId())
				.map(existing -> {
					existing.setTechnician(technician);
					existing.setPlannedStartAt(request.plannedStartAt());
					existing.setPlannedEndAt(request.plannedEndAt());
					return existing;
				})
				.orElse(new Assignment(workOrder, technician, request.plannedStartAt(), request.plannedEndAt()));

		return assignmentRepository.save(assignment);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TechnicianSummaryResponse> getAllTechnicians() {
		return technicianRepository.findAll().stream()
				.map(TechnicianMapper::toSummaryResponse)
				.toList();
	}

	@Override
	@Transactional
	public TechnicianAvailabilityResponse createTechnicianAvailability(UUID id,
	                                                                   CreateTechnicianAvailabilityRequest request) {
		if (!request.startsAt().isBefore(request.endsAt())) {
			throw ApiException.badRequest("La fecha y hora de inicio debe ser anterior a la fecha y hora de fin.");
		}

		Duration duration = Duration.between(request.startsAt(), request.endsAt());
		if (duration.compareTo(MIN_AVAILABILITY_DURATION) < 0) {
			throw ApiException.badRequest("La duración mínima de disponibilidad es de 4 horas.");
		}

		var technician = technicianRepository.findById(id)
				.orElseThrow(() -> ApiException.notFound("No existe técnico asociado al ID " + id));

		TechnicianAvailability technicianAvailability = new TechnicianAvailability(
				technician,
				request.startsAt(),
				request.endsAt()
		);
		technicianAvailability = technicianAvailabilityRepository.save(technicianAvailability);

		return TechnicianMapper.toAvailabilityResponse(technicianAvailability);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TechnicianAvailabilityResponse> getTechnicianAvailability(UUID id,
	                                                                      OffsetDateTime from,
	                                                                      OffsetDateTime to) {
		if (!from.isBefore(to)) {
			throw ApiException.badRequest("La fecha 'from' debe ser anterior a 'to'.");
		}

		if (!technicianRepository.existsById(id)) {
			throw ApiException.notFound("No existe técnico asociado al ID " + id);
		}

		List<TechnicianAvailability> technicianAvailabilities = technicianAvailabilityRepository
				.findAllByTechnicianIdAndRange(id, from, to);

		return technicianAvailabilities.stream()
				.map(TechnicianMapper::toAvailabilityResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<TechnicianAgendaResponse> getTechnicianAgenda(UUID id, OffsetDateTime from, OffsetDateTime to) {
		if (!from.isBefore(to)) {
			throw ApiException.badRequest("La fecha 'from' debe ser anterior a 'to'.");
		}

		if (!technicianRepository.existsById(id)) {
			throw ApiException.notFound("No existe técnico asociado al ID " + id);
		}

		List<Assignment> agenda = assignmentRepository.findAgendaByTechnicianIdAndRange(id, from, to);

		return agenda.stream()
				.map(AssignmentMapper::toTechnicianAgendaResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Technician getAssignedTechnicianForWorkOrder(UUID workOrderId) {
		return assignmentRepository.findByWorkOrderIdWithTechnician(workOrderId)
				.orElseThrow(() -> ApiException.technicianNotAssigned(
						"No existe asignación asociada a la orden de trabajo " + workOrderId
				)).getTechnician();
	}
}
