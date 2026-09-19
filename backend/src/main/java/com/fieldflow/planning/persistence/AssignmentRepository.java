package com.fieldflow.planning.persistence;

import com.fieldflow.planning.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

	Optional<Assignment> findByWorkOrderId(UUID workOrderId);

	@Query("""
			SELECT COUNT(a)
			FROM Assignment a
			WHERE a.technician.id = :technicianId
			  AND a.workOrder.id <> :workOrderId
			  AND a.plannedStartAt < :plannedEndAt
			  AND a.plannedEndAt > :plannedStartAt
			""")
	long countOverlappingAssignments(UUID technicianId, UUID workOrderId,
	                                 OffsetDateTime plannedStartAt, OffsetDateTime plannedEndAt);
}
