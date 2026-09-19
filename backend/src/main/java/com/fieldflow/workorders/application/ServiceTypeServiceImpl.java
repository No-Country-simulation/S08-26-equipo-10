package com.fieldflow.workorders.application;

import com.fieldflow.shared.exception.ApiException;
import com.fieldflow.workorders.api.dto.ServiceTypeResponse;
import com.fieldflow.workorders.application.mapper.ServiceTypeMapper;
import com.fieldflow.workorders.domain.ServiceType;
import com.fieldflow.workorders.persistence.ServiceTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {

	private final ServiceTypeRepository serviceTypeRepository;

	public ServiceTypeServiceImpl(ServiceTypeRepository serviceTypeRepository) {
		this.serviceTypeRepository = serviceTypeRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ServiceTypeResponse> getAllServiceTypes() {
		return serviceTypeRepository.findAll().stream()
				.map(ServiceTypeMapper::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public ServiceType getEntityById(UUID id) {
		return serviceTypeRepository.findById(id)
				.orElseThrow(() -> ApiException.notFound("No existe un tipo de servicio asociado al ID " + id));
	}
}
