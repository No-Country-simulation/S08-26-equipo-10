package com.fieldflow.assets.application;

import com.fieldflow.assets.api.dto.EquipmentDetailResponse;
import com.fieldflow.assets.domain.Equipment;
import com.fieldflow.maintenance.api.dto.CreatePreventiveMaintenancePlanRequest;
import com.fieldflow.maintenance.api.dto.PreventiveMaintenancePlanResponse;

import java.util.List;
import java.util.UUID;

public interface EquipmentService {

	List<EquipmentDetailResponse> getAllEquipments(UUID siteId, UUID clientId);

	PreventiveMaintenancePlanResponse createPreventiveMaintenancePlan(UUID equipmentId,
	                                                                  CreatePreventiveMaintenancePlanRequest request);

	Equipment getEntityById(UUID id);

	List<PreventiveMaintenancePlanResponse> getMaintenancePlansByEquipmentId(UUID equipmentId);
}
