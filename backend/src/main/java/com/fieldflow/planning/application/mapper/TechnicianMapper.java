package com.fieldflow.planning.application.mapper;

import com.fieldflow.planning.api.dto.TechnicianAvailabilityResponse;
import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.planning.domain.Technician;
import com.fieldflow.planning.domain.TechnicianAvailability;

public interface TechnicianMapper {

	TechnicianSummaryResponse toSummaryResponse(Technician entity);

	TechnicianAvailabilityResponse toAvailabilityResponse(TechnicianAvailability entity);
}
