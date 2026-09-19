package com.fieldflow.assets.api.dto;

import java.util.UUID;

public record SiteSummaryResponse(
		UUID id,
		String name,
		String address
) {
}
