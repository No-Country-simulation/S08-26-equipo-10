package com.fieldflow.planning.application.mapper;

import com.fieldflow.planning.api.dto.AssignmentDetailResponse;
import com.fieldflow.planning.api.dto.AssignmentSummaryResponse;
import com.fieldflow.planning.api.dto.TechnicianAgendaResponse;
import com.fieldflow.planning.domain.Assignment;
import com.fieldflow.workorders.application.mapper.WorkOrderMapper;

public final class AssignmentMapper {

	private AssignmentMapper() {
	}

	public static AssignmentSummaryResponse toSummaryResponse(Assignment entity) {
		return entity == null ? null : new AssignmentSummaryResponse(
				entity.getId(),

				// ------------- technician -------------
				TechnicianMapper.toSummaryResponse(entity.getTechnician()),

				entity.getPlannedStartAt(),
				entity.getPlannedEndAt()
		);
	}

	public static AssignmentDetailResponse toDetailResponse(Assignment entity) {
		return entity == null ? null : new AssignmentDetailResponse(
				entity.getId(),
				WorkOrderMapper.toSummaryResponse(entity.getWorkOrder()),
				TechnicianMapper.toSummaryResponse(entity.getTechnician()),
				entity.getPlannedStartAt(),
				entity.getPlannedEndAt()
		);
	}

	public static TechnicianAgendaResponse toTechnicianAgendaResponse(Assignment entity) {
		return entity == null ? null : new TechnicianAgendaResponse(
				entity.getId(),
				entity.getPlannedStartAt(),
				entity.getPlannedEndAt(),
				WorkOrderMapper.toAgendaResponse(entity.getWorkOrder())
		);
	}
}
