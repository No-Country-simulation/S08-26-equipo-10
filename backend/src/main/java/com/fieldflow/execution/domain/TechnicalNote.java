package com.fieldflow.execution.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "technical_note")
public class TechnicalNote {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "intervention_id", nullable = false)
	private Intervention intervention;

	@Column(name = "content", nullable = false)
	private String content;

	protected TechnicalNote() {
	}

	public TechnicalNote(Intervention intervention, String content) {
		this.intervention = intervention;
		this.content = content;
	}

	public UUID getId() {
		return id;
	}

	public Intervention getIntervention() {
		return intervention;
	}

	public String getContent() {
		return content;
	}
}
