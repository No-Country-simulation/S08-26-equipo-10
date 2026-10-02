package com.fieldflow.maintenance.application;

import com.fieldflow.assets.domain.Equipment;
import com.fieldflow.maintenance.api.dto.CreatePreventiveMaintenancePlanRequest;
import com.fieldflow.maintenance.domain.PreventiveMaintenancePlan;
import com.fieldflow.workorders.domain.ServiceType;

import java.util.List;
import java.util.UUID;

public interface PreventiveMaintenancePlanService {

	PreventiveMaintenancePlan createMaintenancePlan(Equipment equipment,
	                                                ServiceType serviceType,
	                                                CreatePreventiveMaintenancePlanRequest request);

	List<PreventiveMaintenancePlan> getMaintenancePlansByEquipmentId(UUID equipmentId);
}
