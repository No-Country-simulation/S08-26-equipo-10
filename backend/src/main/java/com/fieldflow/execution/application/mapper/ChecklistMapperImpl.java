package com.fieldflow.execution.application.mapper;

import com.fieldflow.execution.api.dto.ChecklistAnswerSummaryResponse;
import com.fieldflow.execution.api.dto.ChecklistDetailResponse;
import com.fieldflow.execution.api.dto.ChecklistItemSummaryResponse;
import com.fieldflow.execution.domain.Checklist;
import com.fieldflow.execution.domain.ChecklistItem;
import com.fieldflow.execution.domain.ChecklistItemAnswer;
import org.springframework.stereotype.Component;

@Component
public class ChecklistMapperImpl implements ChecklistMapper {

	@Override
	public ChecklistAnswerSummaryResponse toSummaryResponse(ChecklistItemAnswer entity) {
		if (entity == null) {
			return null;
		}
		return new ChecklistAnswerSummaryResponse(
				entity.getId(),
				toChecklistItemSummaryResponse(entity.getChecklistItem()),
				entity.getValue(),
				entity.getObservation()
		);
	}

	@Override
	public ChecklistDetailResponse toDetailResponse(Checklist entity) {
		if (entity == null) {
			return null;
		}
		return new ChecklistDetailResponse(
				entity.getId(),
				entity.getName(),
				entity.getItems().stream()
						.map(this::toChecklistItemSummaryResponse)
						.toList()
		);
	}

	private ChecklistItemSummaryResponse toChecklistItemSummaryResponse(ChecklistItem entity) {
		if (entity == null) {
			return null;
		}
		return new ChecklistItemSummaryResponse(
				entity.getId(),
				entity.getLabel()
		);
	}
}
