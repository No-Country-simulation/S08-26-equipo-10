package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.TechnicianResponse;
import com.fieldflow.planning.application.mapper.TechnicianMapper;
import com.fieldflow.planning.persistence.TechnicianRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnicianServiceImpl implements TechnicianService {

	private final TechnicianRepository technicianRepository;
	private final TechnicianMapper technicianMapper;

	public TechnicianServiceImpl(TechnicianRepository technicianRepository, TechnicianMapper technicianMapper) {
		this.technicianRepository = technicianRepository;
		this.technicianMapper = technicianMapper;
	}

	@Override
	public List<TechnicianResponse> getAllTechnicians() {
		return technicianRepository.findAll().stream()
				.map(technicianMapper::toResponse)
				.toList();
	}
}
