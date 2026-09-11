package com.fieldflow.execution.api.dto;

import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record InterventionDetailResponse(
		UUID id,
		TechnicianSummaryResponse technician,
		OffsetDateTime startedAt,
		OffsetDateTime endedAt,
		String status,
		String result,
		String observations,
		// TODO: crear records dedicados para cada uno de estos elementos en lugar de usar List<?>
		List<?> failures,
		List<?> repairs,
		List<?> components,
		List<?> checklistResponses,
		List<?> technicalNotes,
		List<?> evidence,
		Object conformity
) {
}
