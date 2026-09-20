package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.AssignmentRequest;
import com.fieldflow.planning.domain.Assignment;
import com.fieldflow.planning.persistence.AssignmentRepository;
import com.fieldflow.planning.persistence.TechnicianAvailabilityRepository;
import com.fieldflow.shared.exception.ApiErrorType;
import com.fieldflow.shared.exception.ApiException;
import com.fieldflow.workorders.domain.WorkOrder;
import org.springframework.stereotype.Service;

@Service
public class AssignmentServiceImp implements AssignmentService {

	private final AssignmentRepository assignmentRepository;
	private final TechnicianAvailabilityRepository availabilityRepository;

	private final TechnicianService technicianService;

	public AssignmentServiceImp(AssignmentRepository assignmentRepository,
	                            TechnicianAvailabilityRepository availabilityRepository,
	                            TechnicianService technicianService) {
		this.assignmentRepository = assignmentRepository;
		this.availabilityRepository = availabilityRepository;
		this.technicianService = technicianService;
	}


	@Override
	public Assignment createAssignment(WorkOrder workOrder, AssignmentRequest request) {
		var technician = technicianService.getTechnicianEntityById(request.technicianId());

		// validates technician availability
		boolean available = availabilityRepository.countContainingAvailability(technician.getId(),
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
}
