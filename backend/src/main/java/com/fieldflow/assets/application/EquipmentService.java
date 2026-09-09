package com.fieldflow.assets.application;

import com.fieldflow.assets.api.dto.EquipmentResponse;

import java.util.List;
import java.util.UUID;

public interface EquipmentService {

	List<EquipmentResponse> getAllEquipments(UUID siteId, UUID clientId);
}
