package com.fieldflow.assets.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "client")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@Column(name = "name", nullable = false, length = 200)
	private String name;

	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
	private List<Site> sites = new ArrayList<>();

	protected Client() {
	}

	public Client(String name) {
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Site> getSites() {
		return sites;
	}
}
