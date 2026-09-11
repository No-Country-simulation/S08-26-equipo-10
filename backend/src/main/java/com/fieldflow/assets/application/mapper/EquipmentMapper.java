package com.fieldflow.assets.application.mapper;

import com.fieldflow.assets.api.dto.EquipmentDetailResponse;
import com.fieldflow.assets.domain.Equipment;

public interface EquipmentMapper {

	EquipmentDetailResponse toResponse(Equipment entity);
}
