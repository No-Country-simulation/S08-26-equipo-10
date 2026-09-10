package com.fieldflow.workorders.application;

import com.fieldflow.workorders.api.dto.WorkOrderResponse;
import com.fieldflow.workorders.domain.WorkOrderStatus;

import java.util.List;
import java.util.UUID;

public interface WorkOrderService {

	List<WorkOrderResponse> getAllWorkOrders(WorkOrderStatus status, UUID equipmentId);
}
