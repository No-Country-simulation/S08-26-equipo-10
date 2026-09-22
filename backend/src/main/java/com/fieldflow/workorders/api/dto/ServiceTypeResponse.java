package com.fieldflow.workorders.api.dto;

import java.util.UUID;

public record ServiceTypeResponse(
		UUID id,
		String name
) {
}