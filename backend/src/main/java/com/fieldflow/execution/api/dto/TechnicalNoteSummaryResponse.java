package com.fieldflow.execution.api.dto;

import java.util.UUID;

public record TechnicalNoteSummaryResponse(
		UUID id,
		String content
) {
}
