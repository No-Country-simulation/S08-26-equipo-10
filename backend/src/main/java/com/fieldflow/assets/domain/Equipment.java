package com.fieldflow.assets.domain;

import com.fieldflow.maintenance.domain.PreventiveMaintenancePlan;
import com.fieldflow.workorders.domain.WorkOrder;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "equipment")
public class Equipment {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "site_id", nullable = false)
	private Site site;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "installation_id")
	private Installation installation;

	@Column(name = "identifier", nullable = false, length = 150)
	private String identifier;

	@Column(name = "name", nullable = false, length = 200)
	private String name;

	@Column(name = "current_status", nullable = false, length = 50)
	private String currentStatus;

	@OneToMany(mappedBy = "equipment", fetch = FetchType.LAZY)
	private List<WorkOrder> workOrders = new ArrayList<>();

	@OneToMany(mappedBy = "equipment", fetch = FetchType.LAZY)
	private List<PreventiveMaintenancePlan> preventiveMaintenancePlans = new ArrayList<>();

	protected Equipment() {
	}

	public Equipment(
			Site site,
			Installation installation,
			String identifier,
			String name,
			String currentStatus
	) {
		this.site = site;
		this.installation = installation;
		this.identifier = identifier;
		this.name = name;
		this.currentStatus = currentStatus;
	}

	public UUID getId() {
		return id;
	}

	public Site getSite() {
		return site;
	}

	public Installation getInstallation() {
		return installation;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getName() {
		return name;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public List<WorkOrder> getWorkOrders() {
		return workOrders;
	}

	public List<PreventiveMaintenancePlan> getPreventiveMaintenancePlans() {
		return preventiveMaintenancePlans;
	}
}
