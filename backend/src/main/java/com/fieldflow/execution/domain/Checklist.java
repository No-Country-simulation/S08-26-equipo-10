package com.fieldflow.execution.domain;

import com.fieldflow.workorders.domain.WorkOrder;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "checklist")
public class Checklist {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "work_order_id", nullable = false, unique = true)
	private WorkOrder workOrder;

	@Column(name = "name", nullable = false, length = 200)
	private String name;

	@OneToMany(mappedBy = "checklist", fetch = FetchType.LAZY)
	private List<ChecklistItem> items = new ArrayList<>();

	protected Checklist() {
	}

	public Checklist(WorkOrder workOrder, String name) {
		this.workOrder = workOrder;
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public String getName() {
		return name;
	}

	public List<ChecklistItem> getItems() {
		return items;
	}
}
