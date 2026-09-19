package com.fieldflow.planning.application;

import com.fieldflow.planning.api.dto.CreateTechnicianAvailabilityRequest;
import com.fieldflow.planning.api.dto.TechnicianAvailabilityResponse;
import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.planning.application.mapper.TechnicianMapper;
import com.fieldflow.planning.domain.Technician;
import com.fieldflow.planning.domain.TechnicianAvailability;
import com.fieldflow.planning.persistence.TechnicianAvailabilityRepository;
import com.fieldflow.planning.persistence.TechnicianRepository;
import com.fieldflow.shared.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TechnicianServiceImpl implements TechnicianService {

	private final Duration MIN_AVAILABILITY_DURATION = Duration.ofMinutes(240);

	private final TechnicianAvailabilityRepository technicianAvailabilityRepository;
	private final TechnicianRepository technicianRepository;

	public TechnicianServiceImpl(TechnicianAvailabilityRepository technicianAvailabilityRepository,
	                             TechnicianRepository technicianRepository) {
		this.technicianAvailabilityRepository = technicianAvailabilityRepository;
		this.technicianRepository = technicianRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TechnicianSummaryResponse> getAllTechnicians() {
		return technicianRepository.findAll().stream()
				.map(TechnicianMapper::toSummaryResponse)
				.toList();
	}

	@Override
	@Transactional
	public TechnicianAvailabilityResponse createTechnicianAvailability(UUID id,
	                                                                   CreateTechnicianAvailabilityRequest request) {
		if (!request.startsAt().isBefore(request.endsAt())) {
			throw ApiException.badRequest("La fecha y hora de inicio debe ser anterior a la fecha y hora de fin.");
		}

		Duration duration = Duration.between(request.startsAt(), request.endsAt());
		if (duration.compareTo(MIN_AVAILABILITY_DURATION) < 0) {
			throw ApiException.badRequest("La duración mínima de disponibilidad es de 4 horas.");
		}

		var technician = technicianRepository.findById(id)
				.orElseThrow(() -> ApiException.notFound("No existe técnico asociado al ID " + id));

		TechnicianAvailability technicianAvailability = new TechnicianAvailability(
				technician,
				request.startsAt(),
				request.endsAt()
		);
		technicianAvailability = technicianAvailabilityRepository.save(technicianAvailability);

		return TechnicianMapper.toAvailabilityResponse(technicianAvailability);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TechnicianAvailabilityResponse> getTechnicianAvailability(UUID id,
	                                                                      OffsetDateTime from,
	                                                                      OffsetDateTime to) {
		if (!from.isBefore(to)) {
			throw ApiException.badRequest("La fecha 'from' debe ser anterior a 'to'.");
		}

		if (!technicianRepository.existsById(id)) {
			throw ApiException.notFound("No existe técnico asociado al ID " + id);
		}

		List<TechnicianAvailability> technicianAvailabilities = technicianAvailabilityRepository
				.findAllByTechnicianIdAndRange(id, from, to);

		return technicianAvailabilities.stream()
				.map(TechnicianMapper::toAvailabilityResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Technician getTechnicianEntityById(UUID id) {
		return technicianRepository.findById(id)
				.orElseThrow(() -> ApiException.notFound("No existe técnico asociado al ID " + id));
	}
}
