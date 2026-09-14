package com.fieldflow.execution.persistence;

import com.fieldflow.execution.domain.ChecklistItemAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChecklistItemAnswerRepository extends JpaRepository<ChecklistItemAnswer, UUID> {

	@Query("""
			SELECT answer
			FROM ChecklistItemAnswer answer
			JOIN FETCH answer.checklistItem item
			WHERE answer.intervention.id IN :interventionIds
			""")
	List<ChecklistItemAnswer> findAllWithItemByInterventionIds(
			@Param("interventionIds") Collection<UUID> interventionIds);
}
