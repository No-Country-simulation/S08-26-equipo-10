package com.fieldflow.execution.application.mapper;

import com.fieldflow.execution.api.dto.InterventionDetailResponse;
import com.fieldflow.execution.application.InterventionDetails;
import com.fieldflow.execution.domain.Intervention;

public interface InterventionMapper {

	InterventionDetailResponse toDetailResponse(Intervention entity, InterventionDetails details);
}
