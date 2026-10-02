package com.fieldflow.execution.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateChecklistRequest(
		@NotBlank(message = "Se debe especificar el nombre de la checklist")
		@Size(
				max = 200,
				message = "El nombre de la checklist no puede superar los 200 caracteres"
		)
		String name,
		@NotNull(message = "Se deben especificar los ítems de la checklist")
		@Valid
		List<CreateChecklistItemRequest> items
) {
}