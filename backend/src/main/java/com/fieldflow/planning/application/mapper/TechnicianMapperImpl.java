package com.fieldflow.planning.application.mapper;

import com.fieldflow.planning.api.dto.TechnicianResponse;
import com.fieldflow.planning.domain.Technician;
import org.springframework.stereotype.Component;

@Component
public class TechnicianMapperImpl implements TechnicianMapper {

	@Override
	public TechnicianResponse toResponse(Technician entity) {
		return new TechnicianResponse(
				entity.getId(),
				entity.getName()
		);
	}
}
