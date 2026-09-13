package com.fieldflow.execution.persistence;

import com.fieldflow.execution.domain.TechnicalNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface TechnicalNoteRepository extends JpaRepository<TechnicalNote, UUID> {

	@Query("""
			SELECT note
			FROM TechnicalNote note
			WHERE note.intervention.id IN :interventionIds
			""")
	List<TechnicalNote> findAllByInterventionIds(@Param("interventionIds") Collection<UUID> interventionIds);
}
