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
	public EquipmentDetailResponse toResponse(Equipment entity) {
		return new EquipmentDetailResponse(
				entity.getId(),
				entity.getIdentifier(),
				entity.getName(),
				entity.getCurrentStatus(),
				toClientSummaryResponse(entity.getSite().getClient()),
				toSiteSummaryResponse(entity.getSite()),
				toInstallationSummaryResponse(entity.getInstallation())
		);
	}

	private EquipmentDetailResponse.ClientSummaryResponse toClientSummaryResponse(Client entity) {
		return new EquipmentDetailResponse.ClientSummaryResponse(
				entity.getId(),
				entity.getName()
		);
	}

	private EquipmentDetailResponse.SiteSummaryResponse toSiteSummaryResponse(Site entity) {
		return new EquipmentDetailResponse.SiteSummaryResponse(
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
