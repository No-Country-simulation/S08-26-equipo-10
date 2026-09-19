package com.fieldflow.planning.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AssignmentRequest(
		@NotNull(message = "Campo technicianId es obligatorio")
		UUID technicianId,

		@NotNull(message = "Campo plannedStartAt es obligatorio")
		OffsetDateTime plannedStartAt,

		@NotNull(message = "Campo plannedEndAt es obligatorio")
		OffsetDateTime plannedEndAt
) {
}
