package com.fieldflow.workorders.application;

import java.util.List;
import java.util.UUID;

import com.fieldflow.workorders.api.dto.ServiceTypeResponse;
import com.fieldflow.workorders.domain.ServiceType;

public interface ServiceTypeService {
    List<ServiceTypeResponse> getAllServiceTypes();

    ServiceType getEntityById(UUID id);
}