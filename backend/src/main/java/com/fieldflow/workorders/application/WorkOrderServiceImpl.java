package com.fieldflow.workorders.application;

import com.fieldflow.assets.application.EquipmentService;
import com.fieldflow.execution.api.dto.ChecklistCreationResponse;
import com.fieldflow.execution.api.dto.CreateChecklistRequest;
import com.fieldflow.execution.application.ChecklistService;
import com.fieldflow.execution.application.InterventionService;
import com.fieldflow.execution.application.mapper.ChecklistMapper;
import com.fieldflow.planning.api.dto.AssignmentDetailResponse;
import com.fieldflow.planning.api.dto.AssignmentRequest;
import com.fieldflow.planning.application.SchedulingService;
import com.fieldflow.planning.application.mapper.AssignmentMapper;
import com.fieldflow.shared.exception.ApiException;
import com.fieldflow.workorders.api.dto.*;
import com.fieldflow.workorders.application.mapper.WorkOrderMapper;
import com.fieldflow.workorders.domain.WorkOrder;
import com.fieldflow.workorders.domain.WorkOrderStatus;
import com.fieldflow.workorders.persistence.WorkOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.fieldflow.workorders.domain.WorkOrderStatus.*;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

	private final WorkOrderRepository repository;

	private final ChecklistService checklistService;
	private final InterventionService interventionService;
	private final EquipmentService equipmentService;
	private final ServiceTypeService serviceTypeService;
	private final SchedulingService schedulingService;

	public WorkOrderServiceImpl(WorkOrderRepository repository,
	                            ChecklistService checklistService,
	                            InterventionService interventionService,
	                            EquipmentService equipmentService,
	                            ServiceTypeService serviceTypeService,
	                            SchedulingService schedulingService) {
		this.repository = repository;
		this.checklistService = checklistService;
		this.interventionService = interventionService;
		this.equipmentService = equipmentService;
		this.serviceTypeService = serviceTypeService;
		this.schedulingService = schedulingService;
	}

	@Override
	@Transactional(readOnly = true)
	public List<WorkOrderSummaryResponse> getAllWorkOrders(WorkOrderStatus status, UUID equipmentId) {
		return repository.findAllWithContext(status, equipmentId).stream()
				.map(WorkOrderMapper::toSummaryResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public WorkOrderDetailResponse getWorkOrderDetailById(UUID id) {
		WorkOrder workOrder = repository.findDetailBaseById(id).orElseThrow(() -> ApiException.notFound(
				"Orden de trabajo no encontrada para ID: " + id
		));

		var checklist = checklistService.getChecklistDetailByWorkOrderId(id);

		var interventions = interventionService.getInterventionsByWorkOrderId(id);

		return WorkOrderMapper.toDetailResponse(workOrder, checklist, interventions);
	}

	@Override
	@Transactional
	public WorkOrderSummaryResponse createWorkOrder(CreateWorkOrderRequest request) {
		var equipment = equipmentService.getEntityById(request.equipmentId());
		var serviceType = serviceTypeService.getEntityById(request.serviceTypeId());

		WorkOrder workOrder = new WorkOrder(
				equipment,
				serviceType,
				request.instructions(),
				request.priority(),
				request.estimatedDurationMinutes(),
				WorkOrderStatus.PENDING
		);
		workOrder = repository.save(workOrder);
		return WorkOrderMapper.toSummaryResponse(workOrder);
	}

	@Override
	@Transactional
	public AssignmentDetailResponse assignWorkOrder(UUID workOrderId, AssignmentRequest request) {
		if (!request.plannedStartAt().isBefore(request.plannedEndAt())) {
			throw ApiException.badRequest("plannedStartAt debe ser anterior a plannedEndAt.");
		}

		var worOrder = repository.findByIdWithContext(workOrderId)
				.orElseThrow(() -> ApiException.notFound("Orden de trabajo no encontrada para ID: " + workOrderId));

		var assignment = schedulingService.createAssignment(worOrder, request);

		worOrder.setStatus(ASSIGNED);

		return AssignmentMapper.toDetailResponse(assignment);
	}

	@Override
	@Transactional
	public WorkOrderStatusResponse updateStatus(UUID id, WorkOrderStatusUpdateRequest request) {
		validatePatchAllowedStatus(request.status());

		var workOrder = repository.findById(id)
				.orElseThrow(() -> ApiException.notFound("No existe una orden de trabajo asociada al ID: " + id));

		var currentStatus = workOrder.getStatus();
		var newStatus = request.status();

		validateCurrentStatus(currentStatus, newStatus);

		workOrder.setStatus(newStatus);

		return new WorkOrderStatusResponse(
				workOrder.getId(),
				workOrder.getStatus()
		);
	}

	@Override
	@Transactional
	public ChecklistCreationResponse createChecklist(UUID id, CreateChecklistRequest request) {
		var workOrder = repository.findById(id)
				.orElseThrow(() -> ApiException.notFound("No existe una orden de trabajo asociada al ID: " + id));

		var checklist = checklistService.createChecklistWithItems(workOrder, request);

		return ChecklistMapper.toCreationResponse(checklist);
	}

	private void validatePatchAllowedStatus(WorkOrderStatus newStatus) {
		if (newStatus != EN_ROUTE && newStatus != RESCHEDULED) {
			throw ApiException.invalidStatusTransition(
					"El estado solicitado no puede establecerse mediante este endpoint."
			);
		}
	}

	private void validateCurrentStatus(WorkOrderStatus currentStatus, WorkOrderStatus newStatus) {
		if (newStatus == EN_ROUTE && currentStatus != ASSIGNED) {
			throw ApiException.invalidStatusTransition(
					"La Orden de Trabajo debe estar en ASSIGNED para pasar a EN_ROUTE."
			);
		}

		if (newStatus == RESCHEDULED && currentStatus != ASSIGNED && currentStatus != EN_ROUTE) {
			throw ApiException.invalidStatusTransition(
					"La Orden de Trabajo no puede reprogramarse desde su estado actual."
			);
		}
	}
}
