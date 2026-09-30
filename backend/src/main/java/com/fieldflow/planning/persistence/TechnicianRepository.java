package com.fieldflow.planning.persistence;

import com.fieldflow.planning.domain.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, UUID> {

	@Query("""
			SELECT t
			FROM Technician t
			WHERE EXISTS (
			    SELECT availability.id
			    FROM TechnicianAvailability availability
			    WHERE availability.technician = t
			      AND availability.startsAt <= :from
			      AND availability.endsAt >= :to
			)
			AND NOT EXISTS (
			    SELECT assignment.id
			    FROM Assignment assignment
			    WHERE assignment.technician = t
			)
			ORDER BY t.name, t.id
			""")
	List<Technician> findAvailableInRange(@Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to);
}
