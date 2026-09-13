package com.fieldflow.conformity.persistence;

import com.fieldflow.conformity.domain.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface EvidenceRepository extends JpaRepository<Evidence, UUID> {

	@Query("""
			SELECT evidence
			FROM Evidence evidence
			WHERE evidence.intervention.id IN :interventionIds
			""")
	List<Evidence> findAllByInterventionIds(@Param("interventionIds") Collection<UUID> interventionIds);
}
