package com.fieldflow.assets.application;

import com.fieldflow.assets.api.dto.EquipmentDetailResponse;
import com.fieldflow.assets.domain.Equipment;

import java.util.List;
import java.util.UUID;

public interface EquipmentService {

	List<EquipmentDetailResponse> getAllEquipments(UUID siteId, UUID clientId);

	Equipment getEntityById(UUID id);
}
