package com.fieldflow.workorders.application;

import com.fieldflow.workorders.api.dto.ServiceTypeResponse;
import com.fieldflow.workorders.domain.ServiceType;

import java.util.List;
import java.util.UUID;

public interface ServiceTypeService {
    List<ServiceTypeResponse> getAllServiceTypes();

    ServiceType getEntityById(UUID id);
}