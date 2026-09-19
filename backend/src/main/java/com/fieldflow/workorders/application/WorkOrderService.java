package com.fieldflow.workorders.application;

import com.fieldflow.workorders.api.dto.CreateWorkOrderRequest;
import com.fieldflow.workorders.api.dto.WorkOrderDetailResponse;
import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;
import com.fieldflow.workorders.domain.WorkOrder;
import com.fieldflow.workorders.domain.WorkOrderStatus;

import java.util.List;
import java.util.UUID;

public interface WorkOrderService {

	List<WorkOrderSummaryResponse> getAllWorkOrders(WorkOrderStatus status, UUID equipmentId);

	WorkOrderDetailResponse getWorkOrderDetailById(UUID id);

	WorkOrderSummaryResponse createWorkOrder(CreateWorkOrderRequest request);

	WorkOrder getWorkOrderEntityById(UUID id);
}
