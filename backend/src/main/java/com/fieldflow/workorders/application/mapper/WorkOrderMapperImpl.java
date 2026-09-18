package com.fieldflow.workorders.application.mapper;

import com.fieldflow.assets.api.dto.EquipmentSummaryResponse;
import com.fieldflow.assets.application.mapper.EquipmentMapper;
import com.fieldflow.execution.api.dto.ChecklistDetailResponse;
import com.fieldflow.execution.api.dto.InterventionDetailResponse;
import com.fieldflow.planning.application.mapper.AssignmentMapper;
import com.fieldflow.workorders.api.dto.WorkOrderDetailResponse;
import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;
import com.fieldflow.workorders.domain.WorkOrder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkOrderMapperImpl implements WorkOrderMapper, WorkOrderSummaryMapper {

	private final EquipmentMapper equipmentMapper;
	private final AssignmentMapper assignmentMapper;
	private final ServiceTypeMapper serviceTypeMapper;

	public WorkOrderMapperImpl(EquipmentMapper equipmentMapper,
	                           AssignmentMapper assignmentMapper,
	                           ServiceTypeMapper serviceTypeMapper) {
		this.equipmentMapper = equipmentMapper;
		this.assignmentMapper = assignmentMapper;
		this.serviceTypeMapper = serviceTypeMapper;
	}

	@Override
	public WorkOrderSummaryResponse toSummaryResponse(WorkOrder entity) {
		if (entity == null) {
			return null;
		}
		return new WorkOrderSummaryResponse(
				entity.getId(),
				toEquipmentSummaryResponse(entity),
				serviceTypeMapper.toResponse(entity.getServiceType()),
				entity.getInstructions(),
				entity.getPriority(),
				entity.getEstimatedDuration(),
				entity.getStatus()
		);
	}

	@Override
	public WorkOrderDetailResponse toDetailResponse(WorkOrder entity,
	                                                ChecklistDetailResponse checklist,
	                                                List<InterventionDetailResponse> interventions) {
		if (entity == null) {
			return null;
		}
		return new WorkOrderDetailResponse(
				entity.getId(),
				entity.getInstructions(),
				entity.getPriority(),
				entity.getEstimatedDuration(),
				entity.getStatus(),

				// ----------------- service-type -----------------
				serviceTypeMapper.toResponse(entity.getServiceType()),

				// ----------------- equipment -----------------
				equipmentMapper.toDetailResponse(entity.getEquipment()),

				// ----------------- assignment -----------------
				assignmentMapper.toSummaryResponse(entity.getAssignment()),

				// ----------------- checklist -----------------
				checklist,

				// ----------------- interventions -----------------
				interventions
		);
	}

	private EquipmentSummaryResponse toEquipmentSummaryResponse(WorkOrder entity) {
		if (entity.getEquipment() == null) {
			return null;
		}
		var equipment = entity.getEquipment();
		return new EquipmentSummaryResponse(
				equipment.getId(),
				equipment.getIdentifier(),
				equipment.getName()
		);
	}
}
