package com.fieldflow.planning.domain;

import com.fieldflow.execution.domain.Intervention;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "technician")
public class Technician {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@Column(name = "name", nullable = false, length = 200)
	private String name;

	@OneToMany(mappedBy = "technician", fetch = FetchType.LAZY)
	private List<TechnicianAvailability> availabilities = new ArrayList<>();

	@OneToMany(mappedBy = "technician", fetch = FetchType.LAZY)
	private List<Assignment> assignments = new ArrayList<>();

	@OneToMany(mappedBy = "technician", fetch = FetchType.LAZY)
	private List<Intervention> interventions = new ArrayList<>();

	protected Technician() {
	}

	public Technician(String name) {
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<TechnicianAvailability> getAvailabilities() {
		return availabilities;
	}

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public List<Intervention> getInterventions() {
		return interventions;
	}
}
