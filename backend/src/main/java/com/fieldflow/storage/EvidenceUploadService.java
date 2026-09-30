package com.fieldflow.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Coordina dos transacciones de PostgreSQL y una llamada al Storage.
 * La subida remota ocurre fuera de cualquier transacción de BD.
 */
@Service
public class EvidenceUploadService {

	private final EvidenceStorage storage;
	private final EvidenceUploadPersistence persistence;

	public EvidenceUploadService(EvidenceStorage storage, EvidenceUploadPersistence persistence) {
		this.storage = storage;
		this.persistence = persistence;
	}

	public String upload(UUID interventionId, MultipartFile file) {
		EvidenceStorage.PreparedImage image = storage.prepare(file);
		String reference = storage.newReference(interventionId, image);

		// El INSERT queda confirmado antes de llamar a Supabase.
		UUID uploadId = persistence.createPending(interventionId, reference);
		storage.upload(reference, image);
		// Si falla esta actualización, permanece la fila provisional para limpieza.
		persistence.markUploaded(uploadId);
		return reference;
	}
}
