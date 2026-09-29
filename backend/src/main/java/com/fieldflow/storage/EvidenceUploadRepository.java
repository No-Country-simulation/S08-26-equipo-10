package com.fieldflow.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvidenceUploadRepository extends JpaRepository<EvidenceUpload, UUID> {

	// Se usará al validar evidence.reference en PUT /interventions/{id}/report.
	Optional<EvidenceUpload> findByInterventionIdAndReference(UUID interventionId, String reference);
}
