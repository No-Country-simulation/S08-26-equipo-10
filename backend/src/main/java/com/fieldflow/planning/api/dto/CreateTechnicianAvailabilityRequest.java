package com.fieldflow.planning.api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record CreateTechnicianAvailabilityRequest(
		@NotNull(message = "El campo 'startsAt' es obligatorio")
		@FutureOrPresent(message = "startsAt no puede estar en el pasado")
		OffsetDateTime startsAt,

		@NotNull(message = "El campo 'endsAt' es obligatorio")
		@FutureOrPresent(message = "endsAt no puede estar en el pasado")
		OffsetDateTime endsAt
) {
}
