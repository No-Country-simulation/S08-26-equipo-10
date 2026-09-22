package com.fieldflow.conformity.domain;

import com.fieldflow.execution.domain.Intervention;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "evidence")
public class Evidence {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "intervention_id", nullable = false)
	private Intervention intervention;

	@Column(name = "type", nullable = false, length = 50)
	private String type;

	@Column(name = "reference", nullable = false)
	private String reference;

	@Column(name = "description")
	private String description;

	protected Evidence() {
	}

	public Evidence(
			Intervention intervention,
			String type,
			String reference,
			String description
	) {
		this.intervention = intervention;
		this.type = type;
		this.reference = reference;
		this.description = description;
	}

	public UUID getId() {
		return id;
	}

	public Intervention getIntervention() {
		return intervention;
	}

	public String getType() {
		return type;
	}

	public String getReference() {
		return reference;
	}

	public String getDescription() {
		return description;
	}
}
