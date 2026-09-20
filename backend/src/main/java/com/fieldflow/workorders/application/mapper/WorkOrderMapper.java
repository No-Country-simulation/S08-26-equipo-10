package com.fieldflow.workorders.application.mapper;

import com.fieldflow.assets.application.mapper.EquipmentMapper;
import com.fieldflow.execution.api.dto.ChecklistDetailResponse;
import com.fieldflow.execution.api.dto.InterventionDetailResponse;
import com.fieldflow.planning.api.dto.AgendaWorkOrderResponse;
import com.fieldflow.planning.application.mapper.AssignmentMapper;
import com.fieldflow.workorders.api.dto.WorkOrderDetailResponse;
import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;
import com.fieldflow.workorders.domain.WorkOrder;

import java.util.List;

public final class WorkOrderMapper {

	private WorkOrderMapper() {
	}

	public static WorkOrderSummaryResponse toSummaryResponse(WorkOrder entity) {
		if (entity == null) {
			return null;
		}
		return new WorkOrderSummaryResponse(
				entity.getId(),
				EquipmentMapper.toSummaryResponse(entity.getEquipment()),
				ServiceTypeMapper.toResponse(entity.getServiceType()),
				entity.getInstructions(),
				entity.getPriority(),
				entity.getEstimatedDuration(),
				entity.getStatus()
		);
	}

	public static WorkOrderDetailResponse toDetailResponse(WorkOrder entity,
	                                                       ChecklistDetailResponse checklist,
	                                                       List<InterventionDetailResponse> interventions) {
		return entity == null ? null : new WorkOrderDetailResponse(
				entity.getId(),
				entity.getInstructions(),
				entity.getPriority(),
				entity.getEstimatedDuration(),
				entity.getStatus(),

				// ----------------- service-type -----------------
				ServiceTypeMapper.toResponse(entity.getServiceType()),

				// ----------------- equipment -----------------
				EquipmentMapper.toDetailResponse(entity.getEquipment()),

				// ----------------- assignment -----------------
				AssignmentMapper.toSummaryResponse(entity.getAssignment()),

				// ----------------- checklist -----------------
				checklist,

				// ----------------- interventions -----------------
				interventions
		);
	}

	public static AgendaWorkOrderResponse toAgendaResponse(WorkOrder entity) {
		return entity == null ? null : new AgendaWorkOrderResponse(
				entity.getId(),
				entity.getStatus(),
				entity.getPriority(),
				entity.getEquipment().getIdentifier(),
				entity.getEquipment().getName(),
				entity.getEquipment().getSite().getName(),
				entity.getEquipment().getSite().getAddress()
		);
	}
}
