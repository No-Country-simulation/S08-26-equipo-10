package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;

import java.util.List;

public interface TechnicianService {

	List<TechnicianSummaryResponse> getAllTechnicians();
}
