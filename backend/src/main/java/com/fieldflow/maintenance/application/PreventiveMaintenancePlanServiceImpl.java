package com.fieldflow.maintenance.application;

import com.fieldflow.assets.domain.Equipment;
import com.fieldflow.maintenance.api.dto.CreatePreventiveMaintenancePlanRequest;
import com.fieldflow.maintenance.domain.PreventiveMaintenancePlan;
import com.fieldflow.maintenance.domain.Recurrence;
import com.fieldflow.maintenance.persistence.PreventiveMaintenancePlanRepository;
import com.fieldflow.workorders.domain.ServiceType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PreventiveMaintenancePlanServiceImpl implements PreventiveMaintenancePlanService {

	private final PreventiveMaintenancePlanRepository preventivePlanRepository;

	public PreventiveMaintenancePlanServiceImpl(PreventiveMaintenancePlanRepository preventivePlanRepository) {
		this.preventivePlanRepository = preventivePlanRepository;
	}

	@Override
	@Transactional
	public PreventiveMaintenancePlan createMaintenancePlan(Equipment equipment,
	                                                       ServiceType serviceType,
	                                                       CreatePreventiveMaintenancePlanRequest request) {
		var maintenancePlan = new PreventiveMaintenancePlan(
				equipment,
				serviceType,
				request.nextExecutionAt()
		);

		var recurrence = new Recurrence(
				maintenancePlan,
				request.recurrence().frequency(),
				request.recurrence().interval()
		);

		maintenancePlan.assignRecurrence(recurrence);

		return preventivePlanRepository.save(maintenancePlan);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PreventiveMaintenancePlan> getMaintenancePlansByEquipmentId(UUID equipmentId) {
		return preventivePlanRepository.findAllByEquipmentIdWithDetails(equipmentId);
	}
}
