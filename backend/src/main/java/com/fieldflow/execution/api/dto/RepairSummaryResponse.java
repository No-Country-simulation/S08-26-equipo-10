package com.fieldflow.execution.api.dto;

import java.util.UUID;

public record RepairSummaryResponse(
		UUID id,
		String description
) {
}
