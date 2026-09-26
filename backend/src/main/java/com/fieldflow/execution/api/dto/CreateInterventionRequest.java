package com.fieldflow.execution.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateInterventionRequest(
		@NotNull(message = "Se debe especificar el ID del técnico")
		UUID technicianId,

		@NotNull(message = "Se debe especificar la fecha de inicio de la intervención")
		OffsetDateTime startedAt
) {
}
