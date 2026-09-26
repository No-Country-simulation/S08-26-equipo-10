package com.fieldflow.execution.application.mapper;

import com.fieldflow.conformity.api.dto.ConformitySummaryResponse;
import com.fieldflow.conformity.api.dto.EvidenceSummaryResponse;
import com.fieldflow.conformity.domain.Conformity;
import com.fieldflow.conformity.domain.Evidence;
import com.fieldflow.execution.api.dto.*;
import com.fieldflow.execution.application.InterventionDetails;
import com.fieldflow.execution.domain.*;
import com.fieldflow.planning.application.mapper.TechnicianMapper;
import com.fieldflow.workorders.application.mapper.WorkOrderMapper;

public final class InterventionMapper {

	private InterventionMapper() {
	}

	public static InterventionDetailResponse toDetailResponse(Intervention entity, InterventionDetails details) {
		return entity == null ? null : new InterventionDetailResponse(
				entity.getId(),

				// ----------------------- technician -----------------------
				TechnicianMapper.toSummaryResponse(entity.getTechnician()),

				entity.getStartedAt(),
				entity.getEndedAt(),
				entity.getStatus(),
				entity.getResult(),
				entity.getObservations(),

				// ----------------------- details -----------------------
				details.failures().stream().map(InterventionMapper::toFailureSummaryResponse).toList(), // failures
				details.repairs().stream().map(InterventionMapper::toRepairSummaryResponse).toList(), // repairs
				details.components().stream().map(InterventionMapper::toComponentSummaryResponse).toList(), // components
				details.answers().stream().map(ChecklistMapper::toSummaryResponse).toList(), // answers
				details.technicalNotes().stream().map(InterventionMapper::toTechnicalNoteSummaryResponse).toList(), // technicalNotes
				details.evidence().stream().map(InterventionMapper::toEvidenceSummaryResponse).toList(), // evidence

				// ----------------------- conformity -----------------------
				toConformitySummaryResponse(entity.getConformity())
		);
	}

	public static InterventionCreatedResponse toCreatedResponse(Intervention entity) {
		return entity == null ? null : new InterventionCreatedResponse(
				entity.getId(),
				entity.getStartedAt(),
				entity.getEndedAt(),
				entity.getStatus(),
				TechnicianMapper.toSummaryResponse(entity.getTechnician()),
				WorkOrderMapper.toSummaryResponse(entity.getWorkOrder())
		);
	}

	private static FailureSummaryResponse toFailureSummaryResponse(Failure entity) {
		return entity == null ? null : new FailureSummaryResponse(
				entity.getId(),
				entity.getDescription()
		);
	}

	private static RepairSummaryResponse toRepairSummaryResponse(Repair entity) {
		return entity == null ? null : new RepairSummaryResponse(
				entity.getId(),
				entity.getDescription()
		);
	}

	private static ComponentSummaryResponse toComponentSummaryResponse(InterventionComponent entity) {
		return entity == null ? null : new ComponentSummaryResponse(
				entity.getId(),
				entity.getComponentName(),
				entity.getAction(),
				entity.getDescription()
		);
	}

	private static TechnicalNoteSummaryResponse toTechnicalNoteSummaryResponse(TechnicalNote entity) {
		return entity == null ? null : new TechnicalNoteSummaryResponse(
				entity.getId(),
				entity.getContent()
		);
	}

	private static EvidenceSummaryResponse toEvidenceSummaryResponse(Evidence entity) {
		return entity == null ? null : new EvidenceSummaryResponse(
				entity.getId(),
				entity.getType(),
				entity.getReference(),
				entity.getDescription()
		);
	}

	private static ConformitySummaryResponse toConformitySummaryResponse(Conformity entity) {
		return entity == null ? null : new ConformitySummaryResponse(
				entity.getId(),
				entity.getSignature()
		);
	}
}
