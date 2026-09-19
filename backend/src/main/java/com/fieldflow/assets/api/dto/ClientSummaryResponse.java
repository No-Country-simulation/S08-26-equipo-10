package com.fieldflow.assets.api.dto;

import java.util.UUID;

public record ClientSummaryResponse(
		UUID id,
		String name
) {
}
