package com.fieldflow.assets.application.mapper;

import com.fieldflow.assets.api.dto.EquipmentResponse;
import com.fieldflow.assets.domain.Equipment;

public interface EquipmentMapper {

	EquipmentResponse toResponse(Equipment entity);
}
