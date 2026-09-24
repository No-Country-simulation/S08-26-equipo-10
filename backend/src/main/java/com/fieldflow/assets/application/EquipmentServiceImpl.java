package com.fieldflow.assets.application;

import com.fieldflow.assets.api.dto.EquipmentDetailResponse;
import com.fieldflow.assets.application.mapper.EquipmentMapper;
import com.fieldflow.assets.domain.Equipment;
import com.fieldflow.assets.persistence.EquipmentRepository;
import com.fieldflow.maintenance.api.dto.CreatePreventiveMaintenancePlanRequest;
import com.fieldflow.maintenance.api.dto.PreventiveMaintenancePlanResponse;
import com.fieldflow.maintenance.application.PreventiveMaintenancePlanService;
import com.fieldflow.maintenance.application.mapper.PreventivePlanMapper;
import com.fieldflow.shared.exception.ApiException;
import com.fieldflow.workorders.application.ServiceTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EquipmentServiceImpl implements EquipmentService {

	private final EquipmentRepository equipmentRepository;

	private final ServiceTypeService serviceTypeService;
	private final PreventiveMaintenancePlanService preventiveMaintenancePlanService;

	public EquipmentServiceImpl(EquipmentRepository equipmentRepository,
	                            ServiceTypeService serviceTypeService,
	                            PreventiveMaintenancePlanService preventiveMaintenancePlanService) {
		this.equipmentRepository = equipmentRepository;
		this.serviceTypeService = serviceTypeService;
		this.preventiveMaintenancePlanService = preventiveMaintenancePlanService;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EquipmentDetailResponse> getAllEquipments(UUID siteId, UUID clientId) {
		return equipmentRepository.findAllWithContext(siteId, clientId)
				.stream()
				.map(EquipmentMapper::toDetailResponse)
				.toList();
	}

	@Override
	@Transactional
	public PreventiveMaintenancePlanResponse createPreventiveMaintenancePlan(
			UUID equipmentId, CreatePreventiveMaintenancePlanRequest request
	) {
		var equipment = getEntityById(equipmentId);
		var serviceType = serviceTypeService.getEntityById(request.serviceTypeId());
		var maintenancePlan = preventiveMaintenancePlanService.createMaintenancePlan(equipment, serviceType, request);

		return PreventivePlanMapper.toResponse(maintenancePlan);
	}

	@Override
	@Transactional(readOnly = true)
	public Equipment getEntityById(UUID id) {
		return equipmentRepository.findById(id)
				.orElseThrow(() -> ApiException.notFound("No existe un equipo asociado al ID " + id));
	}
}
