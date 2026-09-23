package com.fieldflow.assets.application;

import com.fieldflow.assets.api.dto.ClientListItemResponse;

import java.util.List;

public interface ClientService {

	List<ClientListItemResponse> getClients();
}
