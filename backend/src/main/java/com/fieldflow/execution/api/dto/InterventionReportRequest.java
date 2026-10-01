package com.fieldflow.execution.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record InterventionReportRequest(
		@NotNull(message = "Especifique la fecha y hora de finalización")
		OffsetDateTime endedAt,

		@NotBlank(message = "Especifique el resultado de la intervención")
		String result,

		@NotBlank(message = "Especifique al menos una observación")
		String observations,

		List<@NotNull @Valid FailureRequest> failures,

		List<@NotNull @Valid RepairRequest> repairs,

		List<@NotNull @Valid ComponentsRequest> components,

		List<@NotNull @Valid ChecklistAnswerRequest> checklistAnswers,

		List<@NotNull @Valid TechnicalNotesRequest> technicalNotes,

		@Size(max = 5, message = "Máximo 5 evidencias permitidas")
		List<@NotNull @Valid EvidenceRequest> evidence
) {

	public InterventionReportRequest {
		failures = failures == null ? List.of() : failures;
		repairs = repairs == null ? List.of() : repairs;
		components = components == null ? List.of() : components;
		checklistAnswers = checklistAnswers == null ? List.of() : checklistAnswers;
		technicalNotes = technicalNotes == null ? List.of() : technicalNotes;
		evidence = evidence == null ? List.of() : evidence;
	}

	public record FailureRequest(
			@NotNull(message = "Especifique la descripción de la falla")
			String description
	) {
	}

	public record RepairRequest(
			@NotNull(message = "Especifique la descripción de la reparación")
			String description
	) {
	}

	public record ComponentsRequest(
			@NotBlank(message = "Especifique el nombre del componente")
			@Size(max = 200, message = "El nombre del componente no puede superar los 200 caracteres")
			String componentName,

			@NotBlank(message = "Especifique la acción realizada")
			@Size(max = 100, message = "La acción no puede superar los 100 caracteres")
			String action,

			@NotNull(message = "Especifique la descripción del componente")
			String description
	) {
	}

	public record ChecklistAnswerRequest(
			@NotNull(message = "Especifique el id del item del checklist")
			UUID checklistItemId,

			@NotNull(message = "Especifique el valor de la respuesta")
			String value,

			String observation
	) {
	}

	public record TechnicalNotesRequest(
			@NotNull(message = "Especifique el contenido de la nota técnica")
			String content
	) {
	}

	public record EvidenceRequest(
			@NotBlank(message = "Especifique el tipo de evidencia")
			@Size(max = 50, message = "El tipo de evidencia no puede superar los 50 caracteres")
			@Pattern(regexp = "PHOTO", message = "El tipo de evidencia debe ser PHOTO")
			String type,

			@NotBlank(message = "Especifique la referencia de la evidencia")
			String reference,

			String description
	) {
	}
}
