package com.fieldflow.maintenance.application.mapper;

import com.fieldflow.maintenance.api.dto.RecurrenceResponse;
import com.fieldflow.maintenance.domain.Recurrence;

public final class RecurrenceMapper {

	private RecurrenceMapper() {
	}

	public static RecurrenceResponse toResponse(Recurrence entity) {
		return entity == null ? null : new RecurrenceResponse(
				entity.getId(),
				entity.getFrequency(),
				entity.getInterval()
		);
	}
}
