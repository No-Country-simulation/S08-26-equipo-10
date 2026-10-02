package com.fieldflow.execution.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateChecklistItemRequest(
		@NotBlank(message = "Se debe especificar la descripción del ítem")
		String label

) {
}
