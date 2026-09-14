package com.fieldflow.execution.api.dto;

import java.util.UUID;

public record FailureSummaryResponse(
		UUID id,
		String description
) {
}
