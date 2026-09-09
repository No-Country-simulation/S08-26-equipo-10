package com.fieldflow.assets.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "installation")
public class Installation {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "site_id", nullable = false)
	private Site site;

	@Column(name = "name", nullable = false, length = 200)
	private String name;

	public Installation() {
	}

	public UUID getId() {
		return id;
	}

	public Site getSite() {
		return site;
	}

	public String getName() {
		return name;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void setName(String name) {
		this.name = name;
	}
}
