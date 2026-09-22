package com.fieldflow.workorders.api;

import com.fieldflow.shared.annotations.ApiJsonExample;
import com.fieldflow.workorders.api.dto.ServiceTypeResponse;
import com.fieldflow.workorders.application.ServiceTypeService;
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
@RequestMapping("/api/v1/service-types")
@Tag(name = "Tipos de Servicio", description = "Endpoints para la gestión de tipos de servicio.")
public class ServiceTypeController {

	private final ServiceTypeService serviceTypeService;

	public ServiceTypeController(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

	@Operation(
			summary = "Obtener todos los tipos de servicio",
			description = "Obtiene una lista de todos los tipos de servicio disponibles."
	)
	@ApiResponse(responseCode = "200", description = "Lista de tipos de servicio obtenida exitosamente.")
	@ApiJsonExample(
			description = "Ejemplo de respuesta JSON para obtener todos los tipos de servicio.",
			path = "/static/swagger/examples/workorders/list-service-types-200.json",
			summary = "Lista de tipos de servicio"
	)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ServiceTypeResponse>> getAllServiceTypes() {
		return ResponseEntity.ok(serviceTypeService.getAllServiceTypes());
	}
}