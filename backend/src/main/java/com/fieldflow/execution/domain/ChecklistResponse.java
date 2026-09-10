package com.fieldflow.execution.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
		name = "checklist_response",
		uniqueConstraints = @UniqueConstraint(
				name = "uq_checklist_response_intervention_item",
				columnNames = {"intervention_id", "checklist_item_id"}
		)
)
public class ChecklistResponse {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "intervention_id", nullable = false)
	private Intervention intervention;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "checklist_item_id", nullable = false)
	private ChecklistItem checklistItem;

	@Column(name = "value", nullable = false)
	private String value;

	@Column(name = "observation")
	private String observation;

	protected ChecklistResponse() {
	}

	public ChecklistResponse(
			Intervention intervention,
			ChecklistItem checklistItem,
			String value,
			String observation
	) {
		this.intervention = intervention;
		this.checklistItem = checklistItem;
		this.value = value;
		this.observation = observation;
	}

	public UUID getId() {
		return id;
	}

	public Intervention getIntervention() {
		return intervention;
	}

	public ChecklistItem getChecklistItem() {
		return checklistItem;
	}

	public String getValue() {
		return value;
	}

	public String getObservation() {
		return observation;
	}
}
