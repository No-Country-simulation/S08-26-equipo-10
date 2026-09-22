package com.fieldflow.execution.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "intervention_component")
public class InterventionComponent {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "intervention_id", nullable = false)
	private Intervention intervention;

	@Column(name = "component_name", nullable = false, length = 200)
	private String componentName;

	@Column(name = "action", nullable = false, length = 100)
	private String action;

	@Column(name = "description")
	private String description;

	protected InterventionComponent() {
	}

	public InterventionComponent(
			Intervention intervention,
			String componentName,
			String action,
			String description
	) {
		this.intervention = intervention;
		this.componentName = componentName;
		this.action = action;
		this.description = description;
	}

	public UUID getId() {
		return id;
	}

	public Intervention getIntervention() {
		return intervention;
	}

	public String getComponentName() {
		return componentName;
	}

	public String getAction() {
		return action;
	}

	public String getDescription() {
		return description;
	}
}
