package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.CreateTechnicianAvailabilityRequest;
import com.fieldflow.planning.api.dto.TechnicianAvailabilityResponse;
import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.planning.application.mapper.TechnicianMapper;
import com.fieldflow.planning.domain.TechnicianAvailability;
import com.fieldflow.planning.persistence.TechnicianAvailabilityRepository;
import com.fieldflow.planning.persistence.TechnicianRepository;
import com.fieldflow.shared.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class TechnicianServiceImpl implements TechnicianService {

	private final Duration MIN_AVAILABILITY_DURATION = Duration.ofMinutes(240);

	private final TechnicianAvailabilityRepository technicianAvailabilityRepository;
	private final TechnicianRepository technicianRepository;
	private final TechnicianMapper technicianMapper;

	public TechnicianServiceImpl(TechnicianAvailabilityRepository technicianAvailabilityRepository,
	                             TechnicianRepository technicianRepository,
	                             TechnicianMapper technicianMapper) {
		this.technicianAvailabilityRepository = technicianAvailabilityRepository;
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

	@Override
	@Transactional
	public TechnicianAvailabilityResponse createTechnicianAvailability(UUID technicianId,
	                                                                   CreateTechnicianAvailabilityRequest request) {
		if (!request.startsAt().isBefore(request.endsAt())) {
			throw ApiException.badRequest("La fecha y hora de inicio debe ser anterior a la fecha y hora de fin.");
		}

		Duration duration = Duration.between(request.startsAt(), request.endsAt());
		if (duration.compareTo(MIN_AVAILABILITY_DURATION) < 0) {
			throw ApiException.badRequest("La duración mínima de disponibilidad es de 4 horas.");
		}

		var technician = technicianRepository.findById(technicianId)
				.orElseThrow(() -> ApiException.notFound("No existe técnico asociado al ID " + technicianId));

		TechnicianAvailability technicianAvailability = new TechnicianAvailability(
				technician,
				request.startsAt(),
				request.endsAt()
		);
		technicianAvailability = technicianAvailabilityRepository.save(technicianAvailability);

		return technicianMapper.toAvailabilityResponse(technicianAvailability);
	}
}
