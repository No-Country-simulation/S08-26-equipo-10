package com.fieldflow.conformity.api.dto;

import java.util.UUID;

public record EvidenceSummaryResponse(
		UUID id,
		String type,
		String reference,
		String description
) {
}
