package com.fieldflow.assets.api;

import com.fieldflow.assets.api.dto.ClientListItemResponse;
import com.fieldflow.assets.application.ClientService;
import com.fieldflow.shared.annotations.ApiJsonExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Clientes", description = "Operaciones relacionadas con los clientes")
public class ClientController {

	private final ClientService clientService;

	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	@Operation(
			summary = "Obtiene la lista de clientes",
			description = "Retorna una lista de clientes"
	)
	@ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente")
	@ApiJsonExample(
			description = "Lista de todos los clientes registrados",
			path = "/static/swagger/examples/assets/list-clients-200.json",
			summary = "Lista de clientes"
	)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClientListItemResponse>> getClients() {
		return ResponseEntity.ok(clientService.getClients());
	}
}
