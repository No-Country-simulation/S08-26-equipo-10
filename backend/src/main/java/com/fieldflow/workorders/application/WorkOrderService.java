package com.fieldflow.workorders.application;

import com.fieldflow.execution.api.dto.ChecklistCreationResponse;
import com.fieldflow.execution.api.dto.CreateChecklistRequest;
import com.fieldflow.execution.api.dto.CreateInterventionRequest;
import com.fieldflow.execution.api.dto.InterventionCreatedResponse;
import com.fieldflow.planning.api.dto.AssignmentDetailResponse;
import com.fieldflow.planning.api.dto.AssignmentRequest;
import com.fieldflow.workorders.api.dto.*;
import com.fieldflow.workorders.domain.WorkOrderStatus;

import java.util.List;
import java.util.UUID;

public interface WorkOrderService {

	List<WorkOrderSummaryResponse> getAllWorkOrders(WorkOrderStatus status, UUID equipmentId);

	WorkOrderDetailResponse getWorkOrderDetailById(UUID id);

	WorkOrderSummaryResponse createWorkOrder(CreateWorkOrderRequest request);

	AssignmentDetailResponse assignWorkOrder(UUID workOrderId, AssignmentRequest request);

	WorkOrderStatusResponse updateStatus(UUID id, WorkOrderStatusUpdateRequest request);

	ChecklistCreationResponse createChecklist(UUID id, CreateChecklistRequest request);

	InterventionCreatedResponse startWorkOrderIntervention(UUID workOrderId, CreateInterventionRequest request);
}
