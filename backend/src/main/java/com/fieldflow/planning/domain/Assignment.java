package com.fieldflow.planning.domain;

import com.fieldflow.workorders.domain.WorkOrder;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "assignment")
public class Assignment {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "work_order_id", nullable = false, unique = true)
	private WorkOrder workOrder;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "technician_id", nullable = false)
	private Technician technician;

	@Column(name = "planned_start_at", nullable = false)
	private OffsetDateTime plannedStartAt;

	@Column(name = "planned_end_at", nullable = false)
	private OffsetDateTime plannedEndAt;

	protected Assignment() {
	}

	public Assignment(
			WorkOrder workOrder,
			Technician technician,
			OffsetDateTime plannedStartAt,
			OffsetDateTime plannedEndAt
	) {
		this.workOrder = workOrder;
		this.technician = technician;
		this.plannedStartAt = plannedStartAt;
		this.plannedEndAt = plannedEndAt;
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

	public OffsetDateTime getPlannedStartAt() {
		return plannedStartAt;
	}

	public OffsetDateTime getPlannedEndAt() {
		return plannedEndAt;
	}
}
