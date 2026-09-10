package com.fieldflow.execution.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "checklist_item")
public class ChecklistItem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "checklist_id", nullable = false)
	private Checklist checklist;

	@Column(name = "label", nullable = false)
	private String label;

	@OneToMany(mappedBy = "checklistItem", fetch = FetchType.LAZY)
	private List<ChecklistResponse> responses = new ArrayList<>();

	protected ChecklistItem() {
	}

	public ChecklistItem(Checklist checklist, String label) {
		this.checklist = checklist;
		this.label = label;
	}

	public UUID getId() {
		return id;
	}

	public Checklist getChecklist() {
		return checklist;
	}

	public String getLabel() {
		return label;
	}

	public List<ChecklistResponse> getResponses() {
		return responses;
	}
}
