package com.fieldflow.workorders.application;

import com.fieldflow.assets.application.EquipmentService;
import com.fieldflow.execution.application.ChecklistService;
import com.fieldflow.execution.application.InterventionService;
import com.fieldflow.shared.exception.ApiException;
import com.fieldflow.workorders.api.dto.CreateWorkOrderRequest;
import com.fieldflow.workorders.api.dto.WorkOrderDetailResponse;
import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;
import com.fieldflow.workorders.application.mapper.WorkOrderMapper;
import com.fieldflow.workorders.domain.WorkOrder;
import com.fieldflow.workorders.domain.WorkOrderStatus;
import com.fieldflow.workorders.persistence.WorkOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

	private final WorkOrderRepository repository;

	private final ChecklistService checklistService;
	private final InterventionService interventionService;
	private final EquipmentService equipmentService;
	private final ServiceTypeService serviceTypeService;

	public WorkOrderServiceImpl(WorkOrderRepository repository,
	                            ChecklistService checklistService,
	                            InterventionService interventionService,
	                            EquipmentService equipmentService,
	                            ServiceTypeService serviceTypeService) {
		this.repository = repository;
		this.checklistService = checklistService;
		this.interventionService = interventionService;
		this.equipmentService = equipmentService;
		this.serviceTypeService = serviceTypeService;
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
	@Transactional(readOnly = true)
	public WorkOrder getWorkOrderEntityById(UUID id) {
		return repository.findByIdWithContext(id)
				.orElseThrow(() -> ApiException.notFound("Orden de trabajo no encontrada para ID: " + id));
	}
}
