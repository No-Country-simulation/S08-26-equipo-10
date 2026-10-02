package com.fieldflow.planning.persistence;

import com.fieldflow.planning.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
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

	@Query("""
			SELECT a
			FROM Assignment a
			JOIN FETCH a.workOrder wo
			JOIN FETCH wo.equipment e
			JOIN FETCH e.site
			WHERE a.technician.id = :technicianId
			  AND a.plannedStartAt < :to
			  AND a.plannedEndAt > :from
			ORDER BY a.plannedStartAt ASC
			""")
	List<Assignment> findAgendaByTechnicianIdAndRange(@Param("technicianId") UUID technicianId,
	                                                  @Param("from") OffsetDateTime from,
	                                                  @Param("to") OffsetDateTime to);

	@Query("""
			SELECT a
			FROM Assignment a
			JOIN FETCH a.technician
			WHERE a.workOrder.id = :workOrderId
			""")
	Optional<Assignment> findByWorkOrderIdWithTechnician(@Param("workOrderId") UUID workOrderId);
}
