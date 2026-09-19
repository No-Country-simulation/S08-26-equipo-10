package com.fieldflow.workorders.application.mapper;

import com.fieldflow.workorders.api.dto.ServiceTypeResponse;
import com.fieldflow.workorders.domain.ServiceType;

public final class ServiceTypeMapper {

	private ServiceTypeMapper() {
	}

	public static ServiceTypeResponse toResponse(ServiceType entity) {
		return entity == null ? null : new ServiceTypeResponse(
				entity.getId(),
				entity.getName()
		);
	}
}