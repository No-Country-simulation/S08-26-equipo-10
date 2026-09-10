package com.fieldflow.execution.domain;

import com.fieldflow.conformity.domain.Conformity;
import com.fieldflow.conformity.domain.Evidence;
import com.fieldflow.planning.domain.Technician;
import com.fieldflow.workorders.domain.WorkOrder;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "intervention")
public class Intervention {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "work_order_id", nullable = false)
	private WorkOrder workOrder;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "technician_id", nullable = false)
	private Technician technician;

	@Column(name = "started_at", nullable = false)
	private OffsetDateTime startedAt;

	@Column(name = "ended_at")
	private OffsetDateTime endedAt;

	@Column(name = "status", nullable = false, length = 50)
	private String status;

	@Column(name = "result")
	private String result;

	@Column(name = "observations")
	private String observations;

	@OneToMany(mappedBy = "intervention", fetch = FetchType.LAZY)
	private List<Failure> failures = new ArrayList<>();

	@OneToMany(mappedBy = "intervention", fetch = FetchType.LAZY)
	private List<Repair> repairs = new ArrayList<>();

	@OneToMany(mappedBy = "intervention", fetch = FetchType.LAZY)
	private List<InterventionComponent> components = new ArrayList<>();

	@OneToMany(mappedBy = "intervention", fetch = FetchType.LAZY)
	private List<ChecklistResponse> checklistResponses = new ArrayList<>();

	@OneToMany(mappedBy = "intervention", fetch = FetchType.LAZY)
	private List<TechnicalNote> technicalNotes = new ArrayList<>();

	@OneToMany(mappedBy = "intervention", fetch = FetchType.LAZY)
	private List<Evidence> evidence = new ArrayList<>();

	@OneToOne(mappedBy = "intervention", fetch = FetchType.LAZY)
	private Conformity conformity;

	protected Intervention() {
	}

	public Intervention(
			WorkOrder workOrder,
			Technician technician,
			OffsetDateTime startedAt,
			String status
	) {
		this.workOrder = workOrder;
		this.technician = technician;
		this.startedAt = startedAt;
		this.status = status;
	}

	public UUID getId() {
		return id;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public Technician getTechnician() {
		return technician;
	}

	public OffsetDateTime getStartedAt() {
		return startedAt;
	}

	public OffsetDateTime getEndedAt() {
		return endedAt;
	}

	public String getStatus() {
		return status;
	}

	public String getResult() {
		return result;
	}

	public String getObservations() {
		return observations;
	}

	public List<Failure> getFailures() {
		return failures;
	}

	public List<Repair> getRepairs() {
		return repairs;
	}

	public List<InterventionComponent> getComponents() {
		return components;
	}

	public List<ChecklistResponse> getChecklistResponses() {
		return checklistResponses;
	}

	public List<TechnicalNote> getTechnicalNotes() {
		return technicalNotes;
	}

	public List<Evidence> getEvidence() {
		return evidence;
	}

	public Conformity getConformity() {
		return conformity;
	}
}
