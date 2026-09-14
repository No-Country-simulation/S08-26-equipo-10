package com.fieldflow.execution.persistence;

import com.fieldflow.execution.domain.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, UUID> {

	@Query("""
			SELECT DISTINCT checklist
			FROM Checklist checklist
			LEFT JOIN FETCH checklist.items
			WHERE checklist.workOrder.id = :workOrderId
			""")
	Optional<Checklist> findByWorkOrderIdWithItems(@Param("workOrderId") UUID workOrderId);
}
