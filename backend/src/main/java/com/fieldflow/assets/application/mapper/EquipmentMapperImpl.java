package com.fieldflow.assets.application.mapper;

import com.fieldflow.assets.api.dto.EquipmentDetailResponse;
import com.fieldflow.assets.domain.Client;
import com.fieldflow.assets.domain.Equipment;
import com.fieldflow.assets.domain.Installation;
import com.fieldflow.assets.domain.Site;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapperImpl implements EquipmentMapper {

	@Override
	public EquipmentDetailResponse toDetailResponse(Equipment entity) {
		if (entity == null) {
			return null;
		}
		return new EquipmentDetailResponse(
				entity.getId(),
				entity.getIdentifier(),
				entity.getName(),
				entity.getCurrentStatus(),

				// ----------------- client -----------------
				toClientSummaryResponse(entity.getSite().getClient()),

				// ----------------- site -----------------
				toSiteSummaryResponse(entity.getSite()),

				// ----------------- installation -----------------
				toInstallationSummaryResponse(entity.getInstallation())
		);
	}

	private EquipmentDetailResponse.ClientSummaryResponse toClientSummaryResponse(Client entity) {
		return entity == null ? null : new EquipmentDetailResponse.ClientSummaryResponse(
				entity.getId(),
				entity.getName()
		);
	}

	private EquipmentDetailResponse.SiteSummaryResponse toSiteSummaryResponse(Site entity) {
		return entity == null ? null : new EquipmentDetailResponse.SiteSummaryResponse(
				entity.getId(),
				entity.getName(),
				entity.getAddress()
		);
	}

	private EquipmentDetailResponse.InstallationSummaryResponse toInstallationSummaryResponse(Installation entity) {
		return entity == null ? null : new EquipmentDetailResponse.InstallationSummaryResponse(
				entity.getId(),
				entity.getName()
		);
	}
}
