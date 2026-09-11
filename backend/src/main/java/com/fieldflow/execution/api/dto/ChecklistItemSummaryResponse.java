package com.fieldflow.execution.api.dto;

import java.util.UUID;

public record ChecklistItemSummaryResponse(
		UUID id,
		String label
) {
}
