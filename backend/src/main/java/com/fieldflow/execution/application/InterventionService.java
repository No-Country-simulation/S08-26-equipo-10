package com.fieldflow.execution.application;

import com.fieldflow.execution.api.dto.CreateInterventionRequest;
import com.fieldflow.execution.api.dto.InterventionDetailResponse;
import com.fieldflow.execution.domain.Intervention;
import com.fieldflow.workorders.domain.WorkOrder;

import java.util.List;
import java.util.UUID;

public interface InterventionService {

	List<InterventionDetailResponse> getInterventionsByWorkOrderId(UUID workOrderId);

	Intervention recordInterventionStart(WorkOrder workOrder, CreateInterventionRequest request);
}
