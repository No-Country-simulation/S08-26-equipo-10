package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.planning.application.mapper.TechnicianMapper;
import com.fieldflow.planning.persistence.TechnicianRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(readOnly = true)
	public List<TechnicianSummaryResponse> getAllTechnicians() {
		return technicianRepository.findAll().stream()
				.map(technicianMapper::toSummaryResponse)
				.toList();
	}
}
