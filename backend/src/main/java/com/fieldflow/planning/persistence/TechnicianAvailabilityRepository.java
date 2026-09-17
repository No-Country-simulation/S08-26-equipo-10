package com.fieldflow.planning.persistence;

import com.fieldflow.planning.domain.TechnicianAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TechnicianAvailabilityRepository extends JpaRepository<TechnicianAvailability, UUID> {

	@Query("""
			SELECT ta
			FROM TechnicianAvailability ta
			JOIN FETCH ta.technician t
			WHERE t.id = :technicianId
			  AND ta.startsAt < :to
			  AND ta.endsAt > :from
			ORDER BY ta.startsAt ASC
			""")
	List<TechnicianAvailability> findAllByTechnicianIdAndRange(@Param("technicianId") UUID technicianId,
	                                                           @Param("from") OffsetDateTime from,
	                                                           @Param("to") OffsetDateTime to);
}
