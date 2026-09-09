package com.fieldflow.planning.application.mapper;

import com.fieldflow.planning.api.dto.TechnicianResponse;
import com.fieldflow.planning.domain.Technician;

public interface TechnicianMapper {

	TechnicianResponse toResponse(Technician entity);
}
