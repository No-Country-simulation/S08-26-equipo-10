package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.TechnicianResponse;

import java.util.List;

public interface TechnicianService {

	List<TechnicianResponse> getAllTechnicians();
}
