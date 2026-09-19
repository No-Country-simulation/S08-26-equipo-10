package com.fieldflow.execution.application.mapper;

import com.fieldflow.execution.api.dto.ChecklistAnswerSummaryResponse;
import com.fieldflow.execution.api.dto.ChecklistDetailResponse;
import com.fieldflow.execution.api.dto.ChecklistItemSummaryResponse;
import com.fieldflow.execution.domain.Checklist;
import com.fieldflow.execution.domain.ChecklistItem;
import com.fieldflow.execution.domain.ChecklistItemAnswer;

public final class ChecklistMapper {

	private ChecklistMapper() {
	}

	public static ChecklistAnswerSummaryResponse toSummaryResponse(ChecklistItemAnswer entity) {
		return entity == null ? null : new ChecklistAnswerSummaryResponse(
				entity.getId(),
				toChecklistItemSummaryResponse(entity.getChecklistItem()),
				entity.getValue(),
				entity.getObservation()
		);
	}

	public static ChecklistDetailResponse toDetailResponse(Checklist entity) {
		return entity == null ? null : new ChecklistDetailResponse(
				entity.getId(),
				entity.getName(),
				entity.getItems().stream()
						.map(ChecklistMapper::toChecklistItemSummaryResponse)
						.toList()
		);
	}

	private static ChecklistItemSummaryResponse toChecklistItemSummaryResponse(ChecklistItem entity) {
		return entity == null ? null : new ChecklistItemSummaryResponse(
				entity.getId(),
				entity.getLabel()
		);
	}
}
