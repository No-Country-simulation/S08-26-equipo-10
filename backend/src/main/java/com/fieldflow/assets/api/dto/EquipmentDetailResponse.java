package com.fieldflow.assets.api.dto;

import java.util.UUID;

public record EquipmentDetailResponse(
		UUID id,
		String identifier,
		String name,
		String currentStatus,
		ClientSummaryResponse client,
		SiteSummaryResponse site,
		InstallationSummaryResponse installation
) {
}
