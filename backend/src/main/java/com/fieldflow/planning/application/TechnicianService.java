package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.CreateTechnicianAvailabilityRequest;
import com.fieldflow.planning.api.dto.TechnicianAvailabilityResponse;
import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.planning.domain.Technician;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface TechnicianService {

	List<TechnicianSummaryResponse> getAllTechnicians();

	TechnicianAvailabilityResponse createTechnicianAvailability(UUID id, CreateTechnicianAvailabilityRequest request);

	List<TechnicianAvailabilityResponse> getTechnicianAvailability(UUID id, OffsetDateTime from, OffsetDateTime to);

	Technician getTechnicianEntityById(UUID id);
}
