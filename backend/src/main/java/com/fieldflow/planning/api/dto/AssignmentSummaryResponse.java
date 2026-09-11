package com.fieldflow.planning.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AssignmentSummaryResponse(
		UUID id,
		TechnicianSummaryResponse technician,
		OffsetDateTime plannedStartAt,
		OffsetDateTime plannedEndAt
) {
}
