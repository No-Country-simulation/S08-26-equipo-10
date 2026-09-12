package com.fieldflow.workorders.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fieldflow.workorders.domain.ServiceType;
@Repository 
public interface ServiceTypeRepository extends JpaRepository<ServiceType, UUID> {
    
}
