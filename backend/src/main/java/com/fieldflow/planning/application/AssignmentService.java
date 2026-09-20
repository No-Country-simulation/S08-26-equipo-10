package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.AssignmentRequest;
import com.fieldflow.planning.domain.Assignment;
import com.fieldflow.workorders.domain.WorkOrder;

public interface AssignmentService {

	Assignment createAssignment(WorkOrder workOrder, AssignmentRequest request);
}
