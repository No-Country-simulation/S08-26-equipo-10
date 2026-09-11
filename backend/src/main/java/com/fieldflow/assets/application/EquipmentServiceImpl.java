package com.fieldflow.assets.application;

import com.fieldflow.assets.api.dto.EquipmentResponse;
import com.fieldflow.assets.application.mapper.EquipmentMapper;
import com.fieldflow.assets.persistence.EquipmentRepository;
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
	public List<EquipmentResponse> getAllEquipments(UUID siteId, UUID clientId) {
		return equipmentRepository.findAllWithContext(siteId, clientId)
				.stream()
				.map(equipmentMapper::toResponse)
				.toList();
	}
}
