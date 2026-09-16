package com.fieldflow.planning.persistence;

import com.fieldflow.planning.domain.TechnicianAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TechnicianAvailabilityRepository extends JpaRepository<TechnicianAvailability, UUID> {
}
