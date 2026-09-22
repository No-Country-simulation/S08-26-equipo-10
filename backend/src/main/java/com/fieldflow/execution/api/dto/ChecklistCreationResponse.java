package com.fieldflow.execution.api.dto;

import java.util.List;
import java.util.UUID;

public record ChecklistCreationResponse(
		UUID id,
		UUID workOrderId,
		String name,
		List<ChecklistItemSummaryResponse> items
) {
}
