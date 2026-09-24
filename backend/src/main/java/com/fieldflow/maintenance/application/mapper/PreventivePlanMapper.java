package com.fieldflow.maintenance.application.mapper;

import com.fieldflow.maintenance.api.dto.PreventiveMaintenancePlanResponse;
import com.fieldflow.maintenance.domain.PreventiveMaintenancePlan;

public final class PreventivePlanMapper {

	private PreventivePlanMapper() {
	}

	public static PreventiveMaintenancePlanResponse toResponse(PreventiveMaintenancePlan entity) {
		return entity == null ? null : new PreventiveMaintenancePlanResponse(
				entity.getId(),
				entity.getEquipment().getId(),
				entity.getServiceType().getId(),
				entity.getNextExecutionAt(),
				RecurrenceMapper.toResponse(entity.getRecurrence())
		);
	}
}
