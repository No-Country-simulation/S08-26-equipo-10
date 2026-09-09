package com.fieldflow.planning.domain;

import jakarta.persistence.*;

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
}
