package com.fieldflow.assets.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "installation")
public class Installation {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "site_id", nullable = false)
	private Site site;

	@Column(name = "name", nullable = false, length = 200)
	private String name;

	@OneToMany(mappedBy = "installation", fetch = FetchType.LAZY)
	private List<Equipment> equipments = new ArrayList<>();

	protected Installation() {
	}

	public Installation(Site site, String name) {
		this.site = site;
		this.name = name;
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

	public List<Equipment> getEquipments() {
		return equipments;
	}
}
