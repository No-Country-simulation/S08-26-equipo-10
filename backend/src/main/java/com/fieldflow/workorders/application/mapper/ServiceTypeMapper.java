package com.fieldflow.workorders.application.mapper;

import com.fieldflow.workorders.api.dto.ServiceTypeResponse;
import com.fieldflow.workorders.domain.ServiceType;

public interface ServiceTypeMapper {
    ServiceTypeResponse toResponse(ServiceType serviceType);
}