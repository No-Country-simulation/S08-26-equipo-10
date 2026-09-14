package com.fieldflow.execution.application;

import com.fieldflow.execution.api.dto.ChecklistDetailResponse;

import java.util.UUID;

public interface ChecklistService {

	ChecklistDetailResponse getChecklistDetailByWorkOrderId(UUID workOrderId);
}
