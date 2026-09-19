package com.fieldflow.assets.application.mapper;

import com.fieldflow.assets.api.dto.InstallationSummaryResponse;
import com.fieldflow.assets.domain.Installation;

public final class InstallationMapper {

	private InstallationMapper() {
	}

	public static InstallationSummaryResponse toSummaryResponse(Installation entity) {
		return entity == null ? null : new InstallationSummaryResponse(
				entity.getId(),
				entity.getName()
		);
	}
}
