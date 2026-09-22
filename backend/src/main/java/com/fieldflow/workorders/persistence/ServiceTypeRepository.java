package com.fieldflow.workorders.persistence;

import com.fieldflow.workorders.domain.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository 
public interface ServiceTypeRepository extends JpaRepository<ServiceType, UUID> {
    
}
