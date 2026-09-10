package com.fieldflow.workorders.application.mapper;

import com.fieldflow.workorders.api.dto.WorkOrderResponse;
import com.fieldflow.workorders.domain.WorkOrder;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderMapperImpl implements WorkOrderMapper {

	@Override
	public WorkOrderResponse toResponse(WorkOrder entity) {
		return new WorkOrderResponse(
				entity.getId(),
				toEquipmentSummaryResponse(entity),
				toServiceTypeResponse(entity),
				entity.getPriority(),
				entity.getEstimatedDuration(),
				entity.getStatus()
		);
	}

	private WorkOrderResponse.EquipmentSummaryResponse toEquipmentSummaryResponse(WorkOrder entity) {
		var equipment = entity.getEquipment();
		return new WorkOrderResponse.EquipmentSummaryResponse(
				equipment.getId(),
				equipment.getIdentifier(),
				equipment.getName()
		);
	}

	private WorkOrderResponse.ServiceTypeResponse toServiceTypeResponse(WorkOrder entity) {
		var serviceType = entity.getServiceType();
		return new WorkOrderResponse.ServiceTypeResponse(
				serviceType.getId(),
				serviceType.getName()
		);
	}
}
