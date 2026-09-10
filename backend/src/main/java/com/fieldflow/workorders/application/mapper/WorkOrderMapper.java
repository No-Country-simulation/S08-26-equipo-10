package com.fieldflow.workorders.application.mapper;

import com.fieldflow.workorders.api.dto.WorkOrderResponse;
import com.fieldflow.workorders.domain.WorkOrder;

public interface WorkOrderMapper {

	WorkOrderResponse toResponse(WorkOrder entity);
}
