package com.fieldflow.workorders.application.mapper;

import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;
import com.fieldflow.workorders.domain.WorkOrder;

public interface WorkOrderMapper {

	WorkOrderSummaryResponse toSummaryResponse(WorkOrder entity);
}
