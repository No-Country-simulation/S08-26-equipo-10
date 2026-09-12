package com.fieldflow.workorders.application.mapper;

import com.fieldflow.workorders.api.dto.ServiceTypeResponse;
import com.fieldflow.workorders.domain.ServiceType;
import org.springframework.stereotype.Component;

@Component
public class ServiceTypeMapperImpl implements ServiceTypeMapper {

    @Override
    public ServiceTypeResponse toResponse(ServiceType serviceType) {
        if (serviceType == null) {
            return null;
        }
        return new ServiceTypeResponse(
            serviceType.getId(),
            serviceType.getName()
        );
    }
}