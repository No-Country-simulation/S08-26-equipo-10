package com.fieldflow.storage;

import com.fieldflow.execution.application.InterventionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Los métodos públicos se invocan desde EvidenceUploadService a través del
 * proxy de Spring: cada uno abre y confirma su propia transacción de BD.
 */
@Service
public class EvidenceUploadPersistence {

	private final EvidenceUploadRepository repository;
	private final InterventionService interventionService;

	public EvidenceUploadPersistence(EvidenceUploadRepository repository,
	                                 InterventionService interventionService) {
		this.repository = repository;
		this.interventionService = interventionService;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public UUID createPending(UUID interventionId, String reference) {
		interventionService.validateUploadAllowed(interventionId);
		EvidenceUpload upload = repository.save(new EvidenceUpload(interventionId, reference));
		return upload.getId();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void markUploaded(UUID uploadId) {
		EvidenceUpload upload = repository.findById(uploadId)
				.orElseThrow(() -> new IllegalStateException("No existe el registro de subida " + uploadId));
		upload.markUploaded();
	}
}
