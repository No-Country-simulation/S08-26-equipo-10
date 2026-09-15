package com.fieldflow.assets.application;

import com.fieldflow.assets.api.dto.EquipmentDetailResponse;
import com.fieldflow.assets.application.mapper.EquipmentMapper;
import com.fieldflow.assets.domain.Equipment;
import com.fieldflow.assets.persistence.EquipmentRepository;
import com.fieldflow.shared.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EquipmentServiceImpl implements EquipmentService {

	private final EquipmentRepository equipmentRepository;
	private final EquipmentMapper equipmentMapper;

	public EquipmentServiceImpl(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
		this.equipmentRepository = equipmentRepository;
		this.equipmentMapper = equipmentMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EquipmentDetailResponse> getAllEquipments(UUID siteId, UUID clientId) {
		return equipmentRepository.findAllWithContext(siteId, clientId)
				.stream()
				.map(equipmentMapper::toDetailResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Equipment getEntityById(UUID id) {
		return equipmentRepository.findById(id)
				.orElseThrow(() -> ApiException.notFound("No existe un equipo asociado al ID " + id));
	}
}
