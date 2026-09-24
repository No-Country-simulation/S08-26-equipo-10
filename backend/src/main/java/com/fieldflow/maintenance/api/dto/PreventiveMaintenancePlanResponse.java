package com.fieldflow.maintenance.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PreventiveMaintenancePlanResponse(
		UUID id,
		UUID equipmentId,
		UUID serviceTypeId,
		OffsetDateTime nextExecutionAt,
		RecurrenceResponse recurrence
) {
}
