package com.fieldflow.planning.application.mapper;

import com.fieldflow.planning.api.dto.AssignmentSummaryResponse;
import com.fieldflow.planning.domain.Assignment;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMapperImpl implements AssignmentMapper {

	private final TechnicianMapper technicianMapper;

	public AssignmentMapperImpl(TechnicianMapper technicianMapper) {
		this.technicianMapper = technicianMapper;
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
}
