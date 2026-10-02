package com.fieldflow.execution.api.dto;

import com.fieldflow.execution.domain.InterventionStatus;
import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;

import java.time.OffsetDateTime;
import java.util.UUID;

public record InterventionCreatedResponse(
		UUID id,
		OffsetDateTime startedAt,
		OffsetDateTime endedAt,
		InterventionStatus status,
		TechnicianSummaryResponse technician,
		WorkOrderSummaryResponse workOrder
) {
}
