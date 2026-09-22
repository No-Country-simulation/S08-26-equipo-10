package com.fieldflow.planning.api.dto;

import com.fieldflow.workorders.domain.WorkOrderPriority;
import com.fieldflow.workorders.domain.WorkOrderStatus;

import java.util.UUID;

public record AgendaWorkOrderResponse(
		UUID id,
		WorkOrderStatus status,
		WorkOrderPriority priority,
		String equipmentIdentifier,
		String equipmentName,
		String siteName,
		String siteAddress
) {
}
