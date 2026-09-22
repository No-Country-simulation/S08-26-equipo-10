package com.fieldflow.conformity.domain;

import com.fieldflow.execution.domain.Intervention;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "conformity")
public class Conformity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "intervention_id", nullable = false, unique = true)
	private Intervention intervention;

	@Column(name = "signature", nullable = false)
	private String signature;

	protected Conformity() {
	}

	public Conformity(Intervention intervention, String signature) {
		this.intervention = intervention;
		this.signature = signature;
	}

	public UUID getId() {
		return id;
	}

	public Intervention getIntervention() {
		return intervention;
	}

	public String getSignature() {
		return signature;
	}
}
