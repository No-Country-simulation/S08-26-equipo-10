package com.fieldflow.execution.application;

import com.fieldflow.execution.api.dto.ChecklistDetailResponse;
import com.fieldflow.execution.application.mapper.ChecklistMapper;
import com.fieldflow.execution.persistence.ChecklistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ChecklistServiceImpl implements ChecklistService {

	private final ChecklistRepository repository;
	private final ChecklistMapper mapper;

	public ChecklistServiceImpl(ChecklistRepository repository, ChecklistMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	@Transactional(readOnly = true)
	public ChecklistDetailResponse getChecklistDetailByWorkOrderId(UUID workOrderId) {
		return repository.findByWorkOrderIdWithItems(workOrderId)
				.map(mapper::toDetailResponse)
				.orElse(null);
	}
}
