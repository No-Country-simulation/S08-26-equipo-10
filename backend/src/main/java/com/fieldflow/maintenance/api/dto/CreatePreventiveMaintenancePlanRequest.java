package com.fieldflow.maintenance.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreatePreventiveMaintenancePlanRequest(
		@NotNull(message = "Se debe especificar el tipo de servicio")
		UUID serviceTypeId,

		@NotNull(message = "Se debe especificar la próxima fecha de ejecución")
		@Future(message = "La fecha de ejecución debe ser posterior a la fecha actual")
		OffsetDateTime nextExecutionAt,

		@NotNull(message = "Se debe especificar la recurrencia")
		@Valid
		RecurrenceRequest recurrence
) {
}
