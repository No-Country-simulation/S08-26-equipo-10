package com.fieldflow.planning.application.mapper;

import com.fieldflow.planning.api.dto.AssignmentResponse;
import com.fieldflow.planning.api.dto.AssignmentSummaryResponse;
import com.fieldflow.planning.domain.Assignment;
import com.fieldflow.planning.domain.Technician;
import com.fieldflow.workorders.domain.WorkOrder;

public interface AssignmentMapper {

	AssignmentSummaryResponse toSummaryResponse(Assignment entity);

	AssignmentResponse toResponse(Assignment entity, Technician technician, WorkOrder workOrder);
}
