package com.fieldflow.execution.application;

import com.fieldflow.execution.api.dto.ChecklistDetailResponse;
import com.fieldflow.execution.api.dto.CreateChecklistRequest;
import com.fieldflow.execution.application.mapper.ChecklistMapper;
import com.fieldflow.execution.domain.Checklist;
import com.fieldflow.execution.persistence.ChecklistRepository;
import com.fieldflow.shared.exception.ApiException;
import com.fieldflow.workorders.domain.WorkOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ChecklistServiceImpl implements ChecklistService {

	private final ChecklistRepository repository;

	public ChecklistServiceImpl(ChecklistRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public ChecklistDetailResponse getChecklistDetailByWorkOrderId(UUID workOrderId) {
		return repository.findByWorkOrderIdWithItems(workOrderId)
				.map(ChecklistMapper::toDetailResponse)
				.orElse(null);
	}

	@Override
	@Transactional
	public Checklist createChecklistWithItems(WorkOrder workOrder, CreateChecklistRequest request) {
		if (repository.existsByWorkOrderId(workOrder.getId())) {
			throw ApiException.checklistAlreadyExists("La orden de trabajo ya posee una checklist.");
		}

		Checklist checklist = new Checklist(workOrder, request.name());

		request.items().forEach(item -> checklist.addItem(item.label()));

		return repository.save(checklist);
	}
}
