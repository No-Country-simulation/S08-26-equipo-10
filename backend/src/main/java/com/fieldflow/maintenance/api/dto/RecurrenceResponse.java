package com.fieldflow.maintenance.api.dto;

import com.fieldflow.maintenance.domain.RecurrenceFrequency;

import java.util.UUID;

public record RecurrenceResponse(
		UUID id,
		RecurrenceFrequency frequency,
		Integer interval
) {
}
