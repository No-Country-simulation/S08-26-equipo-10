package com.fieldflow.execution.api.dto;

import java.util.UUID;

public record ChecklistAnswerSummaryResponse(
		UUID id,
		ChecklistItemSummaryResponse item,
		String value,
		String observation
) {
}
