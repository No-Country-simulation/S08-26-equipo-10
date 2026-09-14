package com.fieldflow.conformity.api.dto;

import java.util.UUID;

public record ConformitySummaryResponse(
		UUID id,
		String signature
) {
}
