package com.fieldflow.planning.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TechnicianAgendaResponse(
		UUID assignmentId,
		OffsetDateTime plannedStartAt,
		OffsetDateTime plannedEndAt,
		AgendaWorkOrderResponse workOrder
) {
}
