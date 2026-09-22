package com.fieldflow.planning.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record CreateTechnicianAvailabilityRequest(
		@NotNull(message = "El campo 'startsAt' es obligatorio")
		OffsetDateTime startsAt,

		@NotNull(message = "El campo 'endsAt' es obligatorio")
		OffsetDateTime endsAt
) {
}
