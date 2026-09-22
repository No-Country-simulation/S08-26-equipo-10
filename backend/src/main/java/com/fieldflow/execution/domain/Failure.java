package com.fieldflow.execution.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "failure")
public class Failure {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "intervention_id", nullable = false)
	private Intervention intervention;

	@Column(name = "description", nullable = false)
	private String description;

	protected Failure() {
	}

	public Failure(Intervention intervention, String description) {
		this.intervention = intervention;
		this.description = description;
	}

	public UUID getId() {
		return id;
	}

	public Intervention getIntervention() {
		return intervention;
	}

	public String getDescription() {
		return description;
	}
}
