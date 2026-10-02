package com.fieldflow.maintenance.api.dto;

import com.fieldflow.workorders.api.dto.ServiceTypeResponse;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PreventiveMaintenancePlanResponse(
		UUID id,
		ServiceTypeResponse serviceType,
		OffsetDateTime nextExecutionAt,
		RecurrenceResponse recurrence
) {
}
