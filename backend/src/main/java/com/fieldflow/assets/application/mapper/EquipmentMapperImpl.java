package com.fieldflow.assets.application.mapper;

import com.fieldflow.assets.api.dto.EquipmentResponse;
import com.fieldflow.assets.domain.Client;
import com.fieldflow.assets.domain.Equipment;
import com.fieldflow.assets.domain.Installation;
import com.fieldflow.assets.domain.Site;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapperImpl implements EquipmentMapper {

	@Override
	public EquipmentResponse toResponse(Equipment entity) {
		return new EquipmentResponse(
				entity.getId(),
				entity.getIdentifier(),
				entity.getName(),
				entity.getCurrentStatus(),
				toClientSummaryResponse(entity.getSite().getClient()),
				toSiteSummaryResponse(entity.getSite()),
				toInstallationSummaryResponse(entity.getInstallation())
		);
	}

	private EquipmentResponse.ClientSummaryResponse toClientSummaryResponse(Client entity) {
		return new EquipmentResponse.ClientSummaryResponse(
				entity.getId(),
				entity.getName()
		);
	}

	private EquipmentResponse.SiteSummaryResponse toSiteSummaryResponse(Site entity) {
		return new EquipmentResponse.SiteSummaryResponse(
				entity.getId(),
				entity.getName(),
				entity.getAddress()
		);
	}

	private EquipmentResponse.InstallationSummaryResponse toInstallationSummaryResponse(Installation entity) {
		return entity == null ? null : new EquipmentResponse.InstallationSummaryResponse(
				entity.getId(),
				entity.getName()
		);
	}
}
