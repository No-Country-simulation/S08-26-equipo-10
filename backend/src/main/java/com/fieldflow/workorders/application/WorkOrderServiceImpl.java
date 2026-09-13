package com.fieldflow.workorders.application;

import com.fieldflow.execution.application.ChecklistService;
import com.fieldflow.execution.application.InterventionService;
import com.fieldflow.shared.exception.ApiException;
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
	private final WorkOrderMapper mapper;

	private final ChecklistService checklistService;
	private final InterventionService interventionService;

	public WorkOrderServiceImpl(WorkOrderRepository repository,
	                            WorkOrderMapper mapper,
	                            ChecklistService checklistService,
	                            InterventionService interventionService) {
		this.repository = repository;
		this.mapper = mapper;
		this.checklistService = checklistService;
		this.interventionService = interventionService;
	}

	@Override
	@Transactional(readOnly = true)
	public List<WorkOrderSummaryResponse> getAllWorkOrders(WorkOrderStatus status, UUID equipmentId) {
		return repository.findAllWithContext(status, equipmentId).stream()
				.map(mapper::toSummaryResponse)
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

		return mapper.toDetailResponse(workOrder, checklist, interventions);
	}
}
