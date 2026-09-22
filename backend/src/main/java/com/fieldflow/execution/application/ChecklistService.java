package com.fieldflow.execution.application;

import com.fieldflow.execution.api.dto.ChecklistDetailResponse;
import com.fieldflow.execution.api.dto.CreateChecklistRequest;
import com.fieldflow.execution.domain.Checklist;
import com.fieldflow.workorders.domain.WorkOrder;

import java.util.UUID;

public interface ChecklistService {

	ChecklistDetailResponse getChecklistDetailByWorkOrderId(UUID workOrderId);

	Checklist createChecklistWithItems(WorkOrder workOrder, CreateChecklistRequest request);
}
