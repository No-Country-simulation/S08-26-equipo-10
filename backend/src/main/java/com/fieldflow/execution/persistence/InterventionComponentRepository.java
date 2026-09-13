package com.fieldflow.execution.persistence;

import com.fieldflow.execution.domain.InterventionComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface InterventionComponentRepository extends JpaRepository<InterventionComponent, UUID> {

	@Query("""
			SELECT component
			FROM InterventionComponent component
			WHERE component.intervention.id IN :interventionIds
			""")
	List<InterventionComponent> findAllByInterventionIds(@Param("interventionIds") Collection<UUID> interventionIds);
}
