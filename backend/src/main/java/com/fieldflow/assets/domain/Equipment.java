package com.fieldflow.assets.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "equipment")
public class Equipment {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
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

	public Equipment() {
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

	public void setId(UUID id) {
		this.id = id;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void setInstallation(Installation installation) {
		this.installation = installation;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
}
