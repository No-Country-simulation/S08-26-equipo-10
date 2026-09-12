package com.fieldflow.workorders.api.dto;

import com.fieldflow.assets.api.dto.EquipmentDetailResponse;
import com.fieldflow.execution.api.dto.ChecklistDetailResponse;
import com.fieldflow.execution.api.dto.InterventionDetailResponse;
import com.fieldflow.planning.api.dto.AssignmentSummaryResponse;
import com.fieldflow.workorders.domain.WorkOrderPriority;
import com.fieldflow.workorders.domain.WorkOrderStatus;

import java.util.List;
import java.util.UUID;

public record WorkOrderDetailResponse(
		UUID id,
		String instructions,
		WorkOrderPriority priority,
		int estimatedDurationMinutes,
		WorkOrderStatus status,
		WorkOrderSummaryResponse.ServiceTypeResponse serviceType, // TODO: cambiar a record dedicado y no sub-record
		EquipmentDetailResponse equipment,
		AssignmentSummaryResponse assignment,
		ChecklistDetailResponse checklist,
		List<InterventionDetailResponse> interventions
) {
}
