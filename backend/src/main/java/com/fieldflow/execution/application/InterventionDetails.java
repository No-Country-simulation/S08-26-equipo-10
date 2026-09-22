package com.fieldflow.execution.application;

import com.fieldflow.conformity.domain.Evidence;
import com.fieldflow.execution.domain.*;

import java.util.List;

public record InterventionDetails(
		List<Failure> failures,
		List<Repair> repairs,
		List<InterventionComponent> components,
		List<ChecklistItemAnswer> answers,
		List<TechnicalNote> technicalNotes,
		List<Evidence> evidence
) {

	public static InterventionDetails empty() {
		return new InterventionDetails(
				List.of(),
				List.of(),
				List.of(),
				List.of(),
				List.of(),
				List.of()
		);
	}
}
