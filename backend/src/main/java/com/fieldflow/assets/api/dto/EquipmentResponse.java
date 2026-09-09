package com.fieldflow.assets.api.dto;

import java.util.UUID;

public record EquipmentResponse(
		UUID id,
		String identifier,
		String name,
		String currentStatus,
		ClientSummaryResponse client,
		SiteSummaryResponse site,
		InstallationSummaryResponse installation
) {

	public record ClientSummaryResponse(
			UUID id,
			String name
	) {
	}

	public record SiteSummaryResponse(
			UUID id,
			String name,
			String address
	) {
	}

	public record InstallationSummaryResponse(
			UUID id,
			String name
	) {
	}
}
