package com.fieldflow.workorders.api.dto;

import com.fieldflow.assets.api.dto.EquipmentSummaryResponse;
import com.fieldflow.workorders.domain.WorkOrderPriority;
import com.fieldflow.workorders.domain.WorkOrderStatus;

import java.util.UUID;

public record WorkOrderSummaryResponse(
		UUID id,
		EquipmentSummaryResponse equipment,
		ServiceTypeResponse serviceType,
		String instructions,
		WorkOrderPriority priority,
		int estimatedDurationMinutes,
		WorkOrderStatus status
) {
}
