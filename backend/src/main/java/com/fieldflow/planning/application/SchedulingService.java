package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.*;
import com.fieldflow.planning.domain.Assignment;
import com.fieldflow.workorders.domain.WorkOrder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface SchedulingService {

	Assignment createAssignment(WorkOrder workOrder, AssignmentRequest request);

	List<TechnicianSummaryResponse> getAllTechnicians();

	TechnicianAvailabilityResponse createTechnicianAvailability(UUID id, CreateTechnicianAvailabilityRequest request);

	List<TechnicianAvailabilityResponse> getTechnicianAvailability(UUID id, OffsetDateTime from, OffsetDateTime to);

	List<TechnicianAgendaResponse> getTechnicianAgenda(UUID id, OffsetDateTime from, OffsetDateTime to);
}
