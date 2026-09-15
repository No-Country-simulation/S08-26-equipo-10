package com.fieldflow.workorders.api.dto;

import com.fieldflow.workorders.domain.WorkOrderPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateWorkOrderRequest(

		@NotNull(message = "equipmentId es obligatorio")
		UUID equipmentId,

		@NotNull(message = "serviceTypeId es obligatorio")
		UUID serviceTypeId,

		@NotBlank(message = "instructions es obligatorio")
		String instructions,

		@NotNull(message = "priority es obligatorio")
		WorkOrderPriority priority,

		@NotNull(message = "estimatedDurationMinutes es obligatorio")
		@Positive(message = "debe ser mayor que cero")
		Integer estimatedDurationMinutes
) {
}