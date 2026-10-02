package com.fieldflow.assets.application;

import com.fieldflow.assets.api.dto.ClientListItemResponse;
import com.fieldflow.assets.persistence.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;

	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ClientListItemResponse> getClients() {
		return clientRepository.findAllWithSummary();
	}
}
