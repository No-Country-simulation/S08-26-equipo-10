package com.fieldflow.planning.application.mapper;

import com.fieldflow.planning.api.dto.AssignmentSummaryResponse;
import com.fieldflow.planning.domain.Assignment;

public interface AssignmentMapper {

	AssignmentSummaryResponse toSummaryResponse(Assignment entity);
}
