package com.fieldflow.assets.api.dto;

import java.util.UUID;

public record EquipmentSummaryResponse(
		UUID id,
		String identifier,
		String name
) {
}
