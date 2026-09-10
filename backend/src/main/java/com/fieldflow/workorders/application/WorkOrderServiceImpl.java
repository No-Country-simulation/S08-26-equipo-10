package com.fieldflow.workorders.application;

import com.fieldflow.workorders.api.dto.WorkOrderResponse;
import com.fieldflow.workorders.application.mapper.WorkOrderMapper;
import com.fieldflow.workorders.domain.WorkOrderStatus;
import com.fieldflow.workorders.persistence.WorkOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

	private final WorkOrderRepository workOrderRepository;
	private final WorkOrderMapper workOrderMapper;

	public WorkOrderServiceImpl(WorkOrderRepository workOrderRepository, WorkOrderMapper workOrderMapper) {
		this.workOrderRepository = workOrderRepository;
		this.workOrderMapper = workOrderMapper;
	}

	@Override
	public List<WorkOrderResponse> getAllWorkOrders(WorkOrderStatus status, UUID equipmentId) {
		return workOrderRepository.findAllWithContext(status, equipmentId).stream()
				.map(workOrderMapper::toResponse)
				.toList();
	}
}
