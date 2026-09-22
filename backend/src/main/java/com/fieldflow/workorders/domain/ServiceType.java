package com.fieldflow.workorders.domain;

import com.fieldflow.maintenance.domain.PreventiveMaintenancePlan;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "service_type")
public class ServiceType {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@Column(name = "name", nullable = false, length = 150)
	private String name;

	@OneToMany(mappedBy = "serviceType", fetch = FetchType.LAZY)
	private List<WorkOrder> workOrders = new ArrayList<>();

	@OneToMany(mappedBy = "serviceType", fetch = FetchType.LAZY)
	private List<PreventiveMaintenancePlan> preventiveMaintenancePlans = new ArrayList<>();

	protected ServiceType() {
	}

	public ServiceType(String name) {
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<WorkOrder> getWorkOrders() {
		return workOrders;
	}

	public List<PreventiveMaintenancePlan> getPreventiveMaintenancePlans() {
		return preventiveMaintenancePlans;
	}
}
