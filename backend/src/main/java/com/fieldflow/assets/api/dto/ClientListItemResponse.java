package com.fieldflow.assets.api.dto;

import java.util.UUID;

public record ClientListItemResponse(
		UUID id,
		String name,
		long siteCount,
		long equipmentCount
) {
}
