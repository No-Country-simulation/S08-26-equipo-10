package com.fieldflow.maintenance.persistence;

import com.fieldflow.maintenance.domain.PreventiveMaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PreventiveMaintenancePlanRepository extends JpaRepository<PreventiveMaintenancePlan, UUID> {
}
