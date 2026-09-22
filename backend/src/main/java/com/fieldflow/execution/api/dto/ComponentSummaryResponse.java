package com.fieldflow.execution.api.dto;

import java.util.UUID;

public record ComponentSummaryResponse(
		UUID id,
		String componentName,
		String action,
		String description
) {
}
