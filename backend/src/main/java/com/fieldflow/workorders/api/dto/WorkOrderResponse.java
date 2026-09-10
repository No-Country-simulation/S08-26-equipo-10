package com.fieldflow.workorders.api.dto;

import com.fieldflow.workorders.domain.WorkOrderPriority;
import com.fieldflow.workorders.domain.WorkOrderStatus;

import java.util.UUID;

public record WorkOrderResponse(
		UUID id,
		EquipmentSummaryResponse equipment,
		ServiceTypeResponse serviceType,
		WorkOrderPriority priority,
		int estimatedDurationMinutes,
		WorkOrderStatus status
) {

	public record EquipmentSummaryResponse(
			UUID id,
			String identifier,
			String name
	) {
	}

	public record ServiceTypeResponse(
			UUID id,
			String name
	) {
	}
}
