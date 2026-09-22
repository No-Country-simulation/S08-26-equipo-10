package com.fieldflow.maintenance.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "recurrence")
public class Recurrence {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
			name = "preventive_maintenance_plan_id",
			nullable = false,
			unique = true
	)
	private PreventiveMaintenancePlan preventiveMaintenancePlan;

	@Column(name = "\"interval\"", nullable = false)
	private Integer interval;

	@Column(name = "frequency", nullable = false, length = 50)
	private String frequency;

	protected Recurrence() {
	}

	public Recurrence(
			PreventiveMaintenancePlan preventiveMaintenancePlan,
			String frequency,
			Integer interval
	) {
		this.preventiveMaintenancePlan = preventiveMaintenancePlan;
		this.frequency = frequency;
		this.interval = interval;
	}

	public UUID getId() {
		return id;
	}

	public PreventiveMaintenancePlan getPreventiveMaintenancePlan() {
		return preventiveMaintenancePlan;
	}

	public String getFrequency() {
		return frequency;
	}

	public Integer getInterval() {
		return interval;
	}
}
