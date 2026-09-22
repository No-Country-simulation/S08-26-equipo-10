package com.fieldflow.execution.persistence;

import com.fieldflow.execution.domain.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, UUID> {

	@Query("""
			SELECT i
			FROM Intervention i
			JOIN FETCH i.technician technician
			LEFT JOIN FETCH i.conformity conformity
			WHERE i.workOrder.id = :workOrderId
			ORDER BY i.startedAt ASC
			""")
	List<Intervention> findAllByWorkOrderId(@Param("workOrderId") UUID workOrderId);
}
