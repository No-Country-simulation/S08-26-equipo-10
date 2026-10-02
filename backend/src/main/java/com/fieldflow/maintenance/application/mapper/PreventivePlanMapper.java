package com.fieldflow.maintenance.application.mapper;

import com.fieldflow.maintenance.api.dto.PreventiveMaintenancePlanResponse;
import com.fieldflow.maintenance.domain.PreventiveMaintenancePlan;
import com.fieldflow.workorders.application.mapper.ServiceTypeMapper;

public final class PreventivePlanMapper {

	private PreventivePlanMapper() {
	}

	public static PreventiveMaintenancePlanResponse toResponse(PreventiveMaintenancePlan entity) {
		return entity == null ? null : new PreventiveMaintenancePlanResponse(
				entity.getId(),
				ServiceTypeMapper.toResponse(entity.getServiceType()),
				entity.getNextExecutionAt(),
				RecurrenceMapper.toResponse(entity.getRecurrence())
		);
	}
}
