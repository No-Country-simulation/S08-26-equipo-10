package com.fieldflow.workorders.application;

import java.util.List;

import com.fieldflow.workorders.persistence.ServiceTypeRepository;
import com.fieldflow.workorders.domain.ServiceType;

public class ServiceTypeServiceImpl implements ServiceTypeService{

    private final ServiceTypeRepository repository;

    public ServiceTypeServiceImpl(ServiceTypeRepository repository){
        this.repository = repository;
    }

    @Override 
    public List<ServiceType> getAllServiceTypes() {
        return repository.findAll();
    }
    
}
