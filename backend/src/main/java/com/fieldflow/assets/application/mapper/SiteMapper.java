package com.fieldflow.assets.application.mapper;

import com.fieldflow.assets.api.dto.SiteSummaryResponse;
import com.fieldflow.assets.domain.Site;

public final class SiteMapper {

	private SiteMapper() {
	}

	public static SiteSummaryResponse toSummaryResponse(Site entity) {
		return entity == null ? null : new SiteSummaryResponse(
				entity.getId(),
				entity.getName(),
				entity.getAddress()
		);
	}
}
