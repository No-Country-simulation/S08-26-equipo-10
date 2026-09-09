package com.fieldflow.assets.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "client")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private UUID id;

	@Column(name = "name", nullable = false, length = 200)
	private String name;

	public Client() {
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}
