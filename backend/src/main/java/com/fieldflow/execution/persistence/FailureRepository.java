package com.fieldflow.execution.persistence;

import com.fieldflow.execution.domain.Failure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface FailureRepository extends JpaRepository<Failure, UUID> {

	@Query("""
			SELECT f
			FROM Failure f
			WHERE f.intervention.id IN :interventionIds
			""")
	List<Failure> findAllByInterventionIds(@Param("interventionIds") Collection<UUID> interventionIds);
}
