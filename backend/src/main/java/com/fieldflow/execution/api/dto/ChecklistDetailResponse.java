package com.fieldflow.execution.api.dto;

import java.util.List;
import java.util.UUID;

public record ChecklistDetailResponse(
		UUID id,
		String name,
		List<ItemSummaryResponse> items
) {

	public record ItemSummaryResponse(
			UUID id,
			String label
	) {
	}
}
