package com.fieldflow.workorders.application;

import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;
import com.fieldflow.workorders.domain.WorkOrderStatus;

import java.util.List;
import java.util.UUID;

public interface WorkOrderService {

	List<WorkOrderSummaryResponse> getAllWorkOrders(WorkOrderStatus status, UUID equipmentId);
}
