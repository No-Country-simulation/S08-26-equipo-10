package com.fieldflow.planning.application.mapper;

import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.planning.domain.Technician;

public interface TechnicianMapper {

	TechnicianSummaryResponse toSummaryResponse(Technician entity);
}
