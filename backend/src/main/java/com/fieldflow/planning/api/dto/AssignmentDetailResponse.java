package com.fieldflow.planning.api.dto;

import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AssignmentDetailResponse(
		UUID id,
		WorkOrderSummaryResponse workOrder,
		TechnicianSummaryResponse technician,
		OffsetDateTime plannedStartAt,
		OffsetDateTime plannedEndAt
) {
}
