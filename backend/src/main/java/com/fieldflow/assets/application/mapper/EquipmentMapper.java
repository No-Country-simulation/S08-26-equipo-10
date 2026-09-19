package com.fieldflow.assets.application.mapper;

import com.fieldflow.assets.api.dto.EquipmentDetailResponse;
import com.fieldflow.assets.api.dto.EquipmentSummaryResponse;
import com.fieldflow.assets.domain.Equipment;

public final class EquipmentMapper {

	private EquipmentMapper() {
	}

	public static EquipmentDetailResponse toDetailResponse(Equipment entity) {
		return entity == null ? null : new EquipmentDetailResponse(
				entity.getId(),
				entity.getIdentifier(),
				entity.getName(),
				entity.getCurrentStatus(),

				// ----------------- client -----------------
				ClientMapper.toSummaryResponse(entity.getSite().getClient()),

				// ----------------- site -----------------
				SiteMapper.toSummaryResponse(entity.getSite()),

				// ----------------- installation -----------------
				InstallationMapper.toSummaryResponse(entity.getInstallation())
		);
	}

	public static EquipmentSummaryResponse toSummaryResponse(Equipment entity) {
		return entity == null ? null : new EquipmentSummaryResponse(
				entity.getId(),
				entity.getIdentifier(),
				entity.getName()
		);
	}
}
