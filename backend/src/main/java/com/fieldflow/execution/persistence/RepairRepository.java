package com.fieldflow.execution.persistence;

import com.fieldflow.execution.domain.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface RepairRepository extends JpaRepository<Repair, UUID> {

	@Query("""
			SELECT r
			FROM Repair r
			WHERE r.intervention.id IN :interventionIds
			""")
	List<Repair> findAllByInterventionIds(@Param("interventionIds") Collection<UUID> interventionIds);
}
