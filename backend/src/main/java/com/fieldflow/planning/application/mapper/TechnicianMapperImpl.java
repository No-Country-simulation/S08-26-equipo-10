package com.fieldflow.planning.application.mapper;

import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.planning.domain.Technician;
import org.springframework.stereotype.Component;

@Component
public class TechnicianMapperImpl implements TechnicianMapper {

	@Override
	public TechnicianSummaryResponse toSummaryResponse(Technician entity) {
		if (entity == null) {
			return null;
		}
		return new TechnicianSummaryResponse(
				entity.getId(),
				entity.getName()
		);
	}
}
