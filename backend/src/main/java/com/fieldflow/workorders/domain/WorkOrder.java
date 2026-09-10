package com.fieldflow.workorders.domain;

import com.fieldflow.assets.domain.Equipment;
import com.fieldflow.execution.domain.Checklist;
import com.fieldflow.execution.domain.Intervention;
import com.fieldflow.planning.domain.Assignment;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "work_order")
public class WorkOrder {

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

	@Column(name = "instructions", nullable = false)
	private String instructions;

	@Enumerated(EnumType.STRING)
	@Column(name = "priority", nullable = false, length = 50)
	private WorkOrderPriority priority;

	@Column(name = "estimated_duration", nullable = false)
	private Integer estimatedDuration;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 50)
	private WorkOrderStatus status;

	@OneToOne(mappedBy = "workOrder", fetch = FetchType.LAZY)
	private Assignment assignment;

	@OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
	private List<Intervention> interventions = new ArrayList<>();

	@OneToOne(mappedBy = "workOrder", fetch = FetchType.LAZY)
	private Checklist checklist;

	protected WorkOrder() {
	}

	public WorkOrder(
			Equipment equipment,
			ServiceType serviceType,
			String instructions,
			WorkOrderPriority priority,
			Integer estimatedDuration,
			WorkOrderStatus status
	) {
		this.equipment = equipment;
		this.serviceType = serviceType;
		this.instructions = instructions;
		this.priority = priority;
		this.estimatedDuration = estimatedDuration;
		this.status = status;
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

	public String getInstructions() {
		return instructions;
	}

	public WorkOrderPriority getPriority() {
		return priority;
	}

	public Integer getEstimatedDuration() {
		return estimatedDuration;
	}

	public WorkOrderStatus getStatus() {
		return status;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public List<Intervention> getInterventions() {
		return interventions;
	}

	public Checklist getChecklist() {
		return checklist;
	}
}
