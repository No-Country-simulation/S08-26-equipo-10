package com.fieldflow.planning.api;

import com.fieldflow.planning.api.dto.CreateTechnicianAvailabilityRequest;
import com.fieldflow.planning.api.dto.TechnicianAvailabilityResponse;
import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.planning.application.TechnicianService;
import com.fieldflow.shared.annotations.ApiJsonExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/technicians")
@Tag(name = "Técnicos", description = "Endpoints para la gestión de técnicos.")
public class TechnicianController {

	private final TechnicianService technicianService;

	public TechnicianController(TechnicianService technicianService) {
		this.technicianService = technicianService;
	}

	@Operation(
			summary = "Obtener todos los técnicos",
			description = """
					Devuelve una lista de todos los técnicos registrados en el sistema.
					Si no hay técnicos, devuelve una lista vacía.
					"""
	)
	@ApiResponse(responseCode = "200", description = "Lista de técnicos obtenida correctamente.")
	@ApiJsonExample(
			description = "Lista de técnicos presentes en la plataforma.",
			path = "/static/swagger/examples/planning/list-technicians-200.json",
			summary = "Lista de técnicos"
	)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TechnicianSummaryResponse>> getAllTechnicians() {
		List<TechnicianSummaryResponse> technicians = technicianService.getAllTechnicians();
		return ResponseEntity.ok(technicians);
	}

	@Operation(
			summary = "Crear disponibilidad de un técnico",
			description = """
					Crea una nueva disponibilidad para el técnico especificado.
					"""
	)
	@ApiResponse(responseCode = "201", description = "Disponibilidad creada correctamente.")
	@ApiJsonExample(
			status = "201",
			description = "Disponibilidad creada correctamente.",
			path = "/static/swagger/examples/planning/create-technician-availability-201.json",
			summary = "Disponibilidad creada"
	)
	@PostMapping(value = "/{technicianId}/availability", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TechnicianAvailabilityResponse> createTechnicianAvailability(
			@PathVariable UUID technicianId, @Valid @RequestBody CreateTechnicianAvailabilityRequest request
	) {
		TechnicianAvailabilityResponse response = technicianService.createTechnicianAvailability(technicianId, request);

		// TODO: cambiar a fromMethodCall cuando GET /technicians/{technicianId}/availability esté disponible
		URI location = URI.create("/api/v1/technicians/" + technicianId + "/availability");

		return ResponseEntity.created(location).body(response);
	}
}
