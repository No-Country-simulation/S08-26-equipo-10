package com.fieldflow.assets.application.mapper;

import com.fieldflow.assets.api.dto.ClientSummaryResponse;
import com.fieldflow.assets.domain.Client;

public final class ClientMapper {

	private ClientMapper() {
	}

	public static ClientSummaryResponse toSummaryResponse(Client entity) {
		return entity == null ? null : new ClientSummaryResponse(
				entity.getId(),
				entity.getName()
		);
	}
}
