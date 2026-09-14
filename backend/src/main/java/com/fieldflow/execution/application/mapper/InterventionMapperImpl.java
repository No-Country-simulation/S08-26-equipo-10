package com.fieldflow.execution.application.mapper;

import com.fieldflow.conformity.api.dto.ConformitySummaryResponse;
import com.fieldflow.conformity.api.dto.EvidenceSummaryResponse;
import com.fieldflow.conformity.domain.Conformity;
import com.fieldflow.conformity.domain.Evidence;
import com.fieldflow.execution.api.dto.*;
import com.fieldflow.execution.application.InterventionDetails;
import com.fieldflow.execution.domain.*;
import com.fieldflow.planning.application.mapper.TechnicianMapper;
import org.springframework.stereotype.Component;

@Component
public class InterventionMapperImpl implements InterventionMapper {

	private final ChecklistMapper checklistMapper;
	private final TechnicianMapper technicianMapper;

	public InterventionMapperImpl(ChecklistMapper checklistMapper, TechnicianMapper technicianMapper) {
		this.checklistMapper = checklistMapper;
		this.technicianMapper = technicianMapper;
	}

	@Override
	public InterventionDetailResponse toDetailResponse(Intervention entity, InterventionDetails details) {
		if (entity == null) {
			return null;
		}
		return new InterventionDetailResponse(
				entity.getId(),

				// ----------------------- technician -----------------------
				technicianMapper.toSummaryResponse(entity.getTechnician()),

				entity.getStartedAt(),
				entity.getEndedAt(),
				entity.getStatus(),
				entity.getResult(),
				entity.getObservations(),

				// ----------------------- details -----------------------
				details.failures().stream().map(this::toFailureSummaryResponse).toList(), // failures
				details.repairs().stream().map(this::toRepairSummaryResponse).toList(), // repairs
				details.components().stream().map(this::toComponentSummaryResponse).toList(), // components
				details.answers().stream().map(checklistMapper::toSummaryResponse).toList(), // answers
				details.technicalNotes().stream().map(this::toTechnicalNoteSummaryResponse).toList(), // technicalNotes
				details.evidence().stream().map(this::toEvidenceSummaryResponse).toList(), // evidence

				// ----------------------- conformity -----------------------
				toConformitySummaryResponse(entity.getConformity())
		);
	}

	private FailureSummaryResponse toFailureSummaryResponse(Failure entity) {
		if (entity == null) {
			return null;
		}
		return new FailureSummaryResponse(
				entity.getId(),
				entity.getDescription()
		);
	}

	private RepairSummaryResponse toRepairSummaryResponse(Repair entity) {
		if (entity == null) {
			return null;
		}
		return new RepairSummaryResponse(
				entity.getId(),
				entity.getDescription()
		);
	}

	private ComponentSummaryResponse toComponentSummaryResponse(InterventionComponent entity) {
		if (entity == null) {
			return null;
		}
		return new ComponentSummaryResponse(
				entity.getId(),
				entity.getComponentName(),
				entity.getAction(),
				entity.getDescription()
		);
	}

	private TechnicalNoteSummaryResponse toTechnicalNoteSummaryResponse(TechnicalNote entity) {
		if (entity == null) {
			return null;
		}
		return new TechnicalNoteSummaryResponse(
				entity.getId(),
				entity.getContent()
		);
	}

	private EvidenceSummaryResponse toEvidenceSummaryResponse(Evidence entity) {
		if (entity == null) {
			return null;
		}
		return new EvidenceSummaryResponse(
				entity.getId(),
				entity.getType(),
				entity.getReference(),
				entity.getDescription()
		);
	}

	private ConformitySummaryResponse toConformitySummaryResponse(Conformity entity) {
		if (entity == null) {
			return null;
		}
		return new ConformitySummaryResponse(
				entity.getId(),
				entity.getSignature()
		);
	}
}
