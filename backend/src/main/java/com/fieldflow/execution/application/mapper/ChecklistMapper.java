package com.fieldflow.execution.application.mapper;

import com.fieldflow.execution.api.dto.ChecklistAnswerSummaryResponse;
import com.fieldflow.execution.api.dto.ChecklistDetailResponse;
import com.fieldflow.execution.domain.Checklist;
import com.fieldflow.execution.domain.ChecklistItemAnswer;

public interface ChecklistMapper {

	ChecklistAnswerSummaryResponse toSummaryResponse(ChecklistItemAnswer entity);

	ChecklistDetailResponse toDetailResponse(Checklist entity);
}
