package com.fieldflow.execution.api.dto;

import com.fieldflow.conformity.api.dto.ConformitySummaryResponse;
import com.fieldflow.conformity.api.dto.EvidenceSummaryResponse;
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
		List<FailureSummaryResponse> failures,
		List<RepairSummaryResponse> repairs,
		List<ComponentSummaryResponse> components,
		List<ChecklistAnswerSummaryResponse> checklistAnswers,
		List<TechnicalNoteSummaryResponse> technicalNotes,
		List<EvidenceSummaryResponse> evidence,
		ConformitySummaryResponse conformity
) {
}
