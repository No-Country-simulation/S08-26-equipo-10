package com.fieldflow.storage;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface EvidenceUploadRepository extends JpaRepository<EvidenceUpload, UUID> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
			SELECT evidenceUpload
			FROM EvidenceUpload evidenceUpload
			WHERE evidenceUpload.interventionId = :interventionId
			  AND evidenceUpload.reference IN :references
			ORDER BY evidenceUpload.id
			""")
	List<EvidenceUpload> findAllForAttachment(@Param("interventionId") UUID interventionId,
	                                          @Param("references") Collection<String> references);
}
