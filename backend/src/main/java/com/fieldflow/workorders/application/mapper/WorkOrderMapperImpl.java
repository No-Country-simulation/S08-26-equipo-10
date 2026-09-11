package com.fieldflow.workorders.application.mapper;

import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;
import com.fieldflow.workorders.domain.WorkOrder;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderMapperImpl implements WorkOrderMapper {

	@Override
	public WorkOrderSummaryResponse toResponse(WorkOrder entity) {
		return new WorkOrderSummaryResponse(
				entity.getId(),
				toEquipmentSummaryResponse(entity),
				toServiceTypeResponse(entity),
				entity.getPriority(),
				entity.getEstimatedDuration(),
				entity.getStatus()
		);
	}

	private WorkOrderSummaryResponse.EquipmentSummaryResponse toEquipmentSummaryResponse(WorkOrder entity) {
		var equipment = entity.getEquipment();
		return new WorkOrderSummaryResponse.EquipmentSummaryResponse(
				equipment.getId(),
				equipment.getIdentifier(),
				equipment.getName()
		);
	}

	private WorkOrderSummaryResponse.ServiceTypeResponse toServiceTypeResponse(WorkOrder entity) {
		var serviceType = entity.getServiceType();
		return new WorkOrderSummaryResponse.ServiceTypeResponse(
				serviceType.getId(),
				serviceType.getName()
		);
	}
}
