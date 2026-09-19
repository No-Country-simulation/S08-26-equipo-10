package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.AssignmentRequest;
import com.fieldflow.planning.api.dto.AssignmentDetailResponse;

import java.util.UUID;

public interface AssignmentService {

	AssignmentDetailResponse assignWorkOrder(UUID workOrderId, AssignmentRequest request);
}
