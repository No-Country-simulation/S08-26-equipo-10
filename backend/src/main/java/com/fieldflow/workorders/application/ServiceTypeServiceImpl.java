package com.fieldflow.workorders.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fieldflow.workorders.persistence.ServiceTypeRepository;
import com.fieldflow.workorders.domain.ServiceType;

@Service 
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
