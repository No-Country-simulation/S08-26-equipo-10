package com.fieldflow.planning.persistence;

import com.fieldflow.planning.domain.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, UUID> {
}
