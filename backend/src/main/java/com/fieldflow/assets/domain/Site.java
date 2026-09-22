package com.fieldflow.assets.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "site")
public class Site {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;

	@Column(name = "name", nullable = false, length = 200)
	private String name;

	@Column(name = "address", nullable = false)
	private String address;

	@OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
	private List<Installation> installations = new ArrayList<>();

	@OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
	private List<Equipment> equipments = new ArrayList<>();

	protected Site() {
	}

	public Site(Client client, String name, String address) {
		this.client = client;
		this.name = name;
		this.address = address;
	}

	public UUID getId() {
		return id;
	}

	public Client getClient() {
		return client;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public List<Installation> getInstallations() {
		return installations;
	}

	public List<Equipment> getEquipments() {
		return equipments;
	}
}
