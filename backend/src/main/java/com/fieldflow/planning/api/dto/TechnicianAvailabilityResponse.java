package com.fieldflow.planning.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TechnicianAvailabilityResponse(
		UUID id,
		TechnicianSummaryResponse technician,
		OffsetDateTime startsAt,
		OffsetDateTime endsAt
) {
}
