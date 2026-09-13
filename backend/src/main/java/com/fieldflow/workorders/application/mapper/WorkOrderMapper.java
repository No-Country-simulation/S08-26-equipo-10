package com.fieldflow.workorders.application.mapper;

import com.fieldflow.execution.api.dto.ChecklistDetailResponse;
import com.fieldflow.execution.api.dto.InterventionDetailResponse;
import com.fieldflow.workorders.api.dto.WorkOrderDetailResponse;
import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;
import com.fieldflow.workorders.domain.WorkOrder;

import java.util.List;

public interface WorkOrderMapper {

	WorkOrderSummaryResponse toSummaryResponse(WorkOrder entity);

	WorkOrderDetailResponse toDetailResponse(WorkOrder entity,
	                                         ChecklistDetailResponse checklist,
	                                         List<InterventionDetailResponse> interventions);
}
