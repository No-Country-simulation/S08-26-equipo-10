package com.fieldflow.maintenance.persistence;

import com.fieldflow.maintenance.domain.PreventiveMaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PreventiveMaintenancePlanRepository extends JpaRepository<PreventiveMaintenancePlan, UUID> {

	@Query("""
			SELECT p
			FROM PreventiveMaintenancePlan p
			JOIN FETCH p.serviceType
			LEFT JOIN FETCH p.recurrence
			WHERE p.equipment.id = :equipmentId
			ORDER BY p.nextExecutionAt ASC, p.id ASC
			""")
	List<PreventiveMaintenancePlan> findAllByEquipmentIdWithDetails(@Param("equipmentId") UUID equipmentId);
}
