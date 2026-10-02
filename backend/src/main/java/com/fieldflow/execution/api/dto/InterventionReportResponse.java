package com.fieldflow.execution.api.dto;

import com.fieldflow.execution.domain.InterventionStatus;
import com.fieldflow.workorders.domain.WorkOrderStatus;

import java.util.UUID;

public record InterventionReportResponse(
		UUID interventionId,
		InterventionStatus status,
		WorkOrderStatus workOrderStatus
) {
}
