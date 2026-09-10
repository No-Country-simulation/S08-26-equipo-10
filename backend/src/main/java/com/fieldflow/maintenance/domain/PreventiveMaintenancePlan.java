package com.fieldflow.maintenance.domain;

import com.fieldflow.assets.domain.Equipment;
import com.fieldflow.workorders.domain.ServiceType;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "preventive_maintenance_plan")
public class PreventiveMaintenancePlan {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "equipment_id", nullable = false)
	private Equipment equipment;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "service_type_id", nullable = false)
	private ServiceType serviceType;

	@Column(name = "next_execution_at", nullable = false)
	private OffsetDateTime nextExecutionAt;

	@OneToOne(mappedBy = "preventiveMaintenancePlan")
	private Recurrence recurrence;

	protected PreventiveMaintenancePlan() {
	}

	public PreventiveMaintenancePlan(
			Equipment equipment,
			ServiceType serviceType,
			OffsetDateTime nextExecutionAt
	) {
		this.equipment = equipment;
		this.serviceType = serviceType;
		this.nextExecutionAt = nextExecutionAt;
	}

	public UUID getId() {
		return id;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public OffsetDateTime getNextExecutionAt() {
		return nextExecutionAt;
	}

	public Recurrence getRecurrence() {
		return recurrence;
	}
}
