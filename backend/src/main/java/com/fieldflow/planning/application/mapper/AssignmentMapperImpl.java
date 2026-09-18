package com.fieldflow.planning.application.mapper;

import com.fieldflow.planning.api.dto.AssignmentResponse;
import com.fieldflow.planning.api.dto.AssignmentSummaryResponse;
import com.fieldflow.planning.domain.Assignment;
import com.fieldflow.planning.domain.Technician;
import com.fieldflow.workorders.application.mapper.WorkOrderSummaryMapper;
import com.fieldflow.workorders.domain.WorkOrder;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMapperImpl implements AssignmentMapper {

	private final TechnicianMapper technicianMapper;
	private final WorkOrderSummaryMapper workOrderSummaryMapper;

	public AssignmentMapperImpl(TechnicianMapper technicianMapper, WorkOrderSummaryMapper workOrderSummaryMapper) {
		this.technicianMapper = technicianMapper;
		this.workOrderSummaryMapper = workOrderSummaryMapper;
	}

	@Override
	public AssignmentSummaryResponse toSummaryResponse(Assignment entity) {
		if (entity == null) {
			return null;
		}
		return new AssignmentSummaryResponse(
				entity.getId(),

				// ------------- technician -------------
				technicianMapper.toSummaryResponse(entity.getTechnician()),

				entity.getPlannedStartAt(),
				entity.getPlannedEndAt()
		);
	}

	@Override
	public AssignmentResponse toResponse(Assignment entity, Technician technician, WorkOrder workOrder) {
		if (entity == null) {
			return null;
		}
		return new AssignmentResponse(
				entity.getId(),
				workOrderSummaryMapper.toSummaryResponse(workOrder),
				technicianMapper.toSummaryResponse(technician),
				entity.getPlannedStartAt(),
				entity.getPlannedEndAt()
		);
	}
}
