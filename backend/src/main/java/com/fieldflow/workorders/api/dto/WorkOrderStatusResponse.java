package com.fieldflow.workorders.api.dto;

import com.fieldflow.workorders.domain.WorkOrderStatus;

import java.util.UUID;

public record WorkOrderStatusResponse(
		UUID id,
		WorkOrderStatus status
) {
}
