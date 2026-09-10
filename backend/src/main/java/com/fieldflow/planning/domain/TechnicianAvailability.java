package com.fieldflow.planning.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "technician_availability")
public class TechnicianAvailability {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "technician_id", nullable = false)
	private Technician technician;

	@Column(name = "starts_at", nullable = false)
	private OffsetDateTime startsAt;

	@Column(name = "ends_at", nullable = false)
	private OffsetDateTime endsAt;

	protected TechnicianAvailability() {
	}

	public TechnicianAvailability(
			Technician technician,
			OffsetDateTime startsAt,
			OffsetDateTime endsAt
	) {
		this.technician = technician;
		this.startsAt = startsAt;
		this.endsAt = endsAt;
	}

	public UUID getId() {
		return id;
	}

	public Technician getTechnician() {
		return technician;
	}

	public OffsetDateTime getStartsAt() {
		return startsAt;
	}

	public OffsetDateTime getEndsAt() {
		return endsAt;
	}
}
