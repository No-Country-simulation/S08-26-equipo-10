package com.fieldflow.assets.application;

import com.fieldflow.assets.api.dto.EquipmentDetailResponse;

import java.util.List;
import java.util.UUID;

public interface EquipmentService {

	List<EquipmentDetailResponse> getAllEquipments(UUID siteId, UUID clientId);
}
