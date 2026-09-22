package com.fieldflow.execution.application;

import com.fieldflow.execution.api.dto.InterventionDetailResponse;

import java.util.List;
import java.util.UUID;

public interface InterventionService {

	List<InterventionDetailResponse> getInterventionsByWorkOrderId(UUID workOrderId);
}
