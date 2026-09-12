package com.fieldflow.workorders.application.mapper;

import com.fieldflow.assets.api.dto.EquipmentDetailResponse;
import com.fieldflow.assets.application.mapper.EquipmentMapper;
import com.fieldflow.planning.api.dto.AssignmentSummaryResponse;
import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.workorders.api.dto.WorkOrderDetailResponse;
import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;
import com.fieldflow.workorders.domain.WorkOrder;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderMapperImpl implements WorkOrderMapper {

	private final EquipmentMapper equipmentMapper;

	public WorkOrderMapperImpl(EquipmentMapper equipmentMapper) {
		this.equipmentMapper = equipmentMapper;
	}

	@Override
	public WorkOrderSummaryResponse toSummaryResponse(WorkOrder entity) {
		return new WorkOrderSummaryResponse(
				entity.getId(),
				toEquipmentSummaryResponse(entity),
				toServiceTypeResponse(entity),
				entity.getPriority(),
				entity.getEstimatedDuration(),
				entity.getStatus()
		);
	}

	@Override
	public WorkOrderDetailResponse toDetailResponse(WorkOrder entity) {
		return new WorkOrderDetailResponse(
				entity.getId(),
				entity.getInstructions(),
				entity.getPriority(),
				entity.getEstimatedDuration(),
				entity.getStatus(),
				new WorkOrderSummaryResponse.ServiceTypeResponse(
						entity.getServiceType().getId(),
						entity.getServiceType().getName()
				),
				equipmentMapper.toResponse(entity.getEquipment()),
				new AssignmentSummaryResponse(
						entity.getAssignment().getId(),
						new TechnicianSummaryResponse(
								entity.getAssignment().getTechnician().getId(),
								entity.getAssignment().getTechnician().getName()
						),
						entity.getAssignment().getPlannedStartAt(),
						entity.getAssignment().getPlannedEndAt()
				),

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
