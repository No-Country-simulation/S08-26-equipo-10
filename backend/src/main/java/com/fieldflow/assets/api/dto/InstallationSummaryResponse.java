package com.fieldflow.assets.api.dto;

import java.util.UUID;

public record InstallationSummaryResponse(
		UUID id,
		String name
) {
}
