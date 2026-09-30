package com.fieldflow.storage;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

/**
 * Registro provisional de un objeto en Storage antes de asociarlo a evidence.
 * interventionId y evidenceId son identificadores escalares para evitar cargar
 * asociaciones JPA al registrar una subida.
 */
@Entity
@Table(name = "evidence_upload")
public class EvidenceUpload {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private UUID id;

	@Column(name = "intervention_id", nullable = false)
	private UUID interventionId;

	@Column(name = "reference", nullable = false, unique = true, columnDefinition = "text")
	private String reference;

	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@Column(name = "uploaded_at")
	private OffsetDateTime uploadedAt;

	@Column(name = "attached_at")
	private OffsetDateTime attachedAt;

	@Column(name = "evidence_id")
	private UUID evidenceId;

	protected EvidenceUpload() {
	}

	public EvidenceUpload(UUID interventionId, String reference) {
		this.interventionId = interventionId;
		this.reference = reference;
		this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
	}

	public UUID getId() {
		return id;
	}

	public UUID getInterventionId() {
		return interventionId;
	}

	public String getReference() {
		return reference;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUploadedAt() {
		return uploadedAt;
	}

	public OffsetDateTime getAttachedAt() {
		return attachedAt;
	}

	public UUID getEvidenceId() {
		return evidenceId;
	}

	public void markUploaded() {
		if (uploadedAt == null) {
			uploadedAt = OffsetDateTime.now(ZoneOffset.UTC);
		}
	}

	public void attachTo(UUID evidenceId, OffsetDateTime attachedAt) {
		if (uploadedAt == null || this.attachedAt != null || this.evidenceId != null) {
			throw new IllegalStateException(
					"La subida no está disponible para asociarse.");
		}

		UUID validEvidenceId = Objects.requireNonNull(evidenceId, "evidenceId");
		OffsetDateTime validAttachedAt = Objects.requireNonNull(attachedAt, "attachedAt");

		this.evidenceId = validEvidenceId;
		this.attachedAt = validAttachedAt;
	}
}
