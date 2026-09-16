package com.fieldflow.planning.application.mapper;

import com.fieldflow.planning.api.dto.TechnicianAvailabilityResponse;
import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.planning.domain.Technician;
import com.fieldflow.planning.domain.TechnicianAvailability;
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

	@Override
	public TechnicianAvailabilityResponse toAvailabilityResponse(TechnicianAvailability entity) {
		if (entity == null) {
			return null;
		}
		return new TechnicianAvailabilityResponse(
				entity.getId(),
				toSummaryResponse(entity.getTechnician()),
				entity.getStartsAt(),
				entity.getEndsAt()
		);
	}
}
