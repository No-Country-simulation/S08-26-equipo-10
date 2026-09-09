package com.fieldflow.assets.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "site")
public class Site {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;

	@Column(name = "name", nullable = false, length = 200)
	private String name;

	@Column(name = "address", nullable = false)
	private String address;

	public Site() {
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

	public void setId(UUID id) {
		this.id = id;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
