package com.fieldflow.maintenance.api.dto;

import com.fieldflow.maintenance.domain.RecurrenceFrequency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecurrenceRequest(
		@NotNull(message = "Se debe especificar la frecuencia")
		RecurrenceFrequency frequency,

		@NotNull(message = "Se debe especificar el intervalo")
		@Positive(message = "El intervalo debe ser mayor que cero")
		Integer interval
) {
}
