package com.fieldflow.workorders.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fieldflow.workorders.persistence.ServiceTypeRepository;
import com.fieldflow.workorders.api.dto.ServiceTypeResponse;
import com.fieldflow.workorders.application.mapper.ServiceTypeMapper;

@Service 
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeMapper serviceTypeMapper;

    public ServiceTypeServiceImpl(ServiceTypeRepository serviceTypeRepository, ServiceTypeMapper serviceTypeMapper) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceTypeMapper = serviceTypeMapper;
    }

    @Override 
    public List<ServiceTypeResponse> getAllServiceTypes() {
        return serviceTypeRepository.findAll().stream()
                .map(serviceTypeMapper::toResponse)
                .toList();
    }
}
