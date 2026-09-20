package com.fieldflow.planning.api;

import com.fieldflow.planning.api.dto.CreateTechnicianAvailabilityRequest;
import com.fieldflow.planning.api.dto.TechnicianAgendaResponse;
import com.fieldflow.planning.api.dto.TechnicianAvailabilityResponse;
import com.fieldflow.planning.api.dto.TechnicianSummaryResponse;
import com.fieldflow.planning.application.SchedulingService;
import com.fieldflow.shared.annotations.ApiJsonExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/api/v1/technicians")
@Tag(name = "Técnicos", description = "Endpoints para la gestión de técnicos.")
public class TechnicianController {

	private final SchedulingService schedulingService;

	public TechnicianController(SchedulingService schedulingService) {
		this.schedulingService = schedulingService;
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
		List<TechnicianSummaryResponse> technicians = schedulingService.getAllTechnicians();
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
		TechnicianAvailabilityResponse response = schedulingService.createTechnicianAvailability(technicianId, request);

		URI location = fromMethodCall(on(TechnicianController.class)
				.getTechnicianAvailability(technicianId, response.startsAt(), response.endsAt())).build().toUri();

		return ResponseEntity.created(location).body(response);
	}

	@Operation(
			summary = "Obtener disponibilidad de un técnico",
			description = """
					Devuelve la disponibilidad del técnico especificado en el rango de fecha y hora indicado.
					"""
	)
	@ApiResponse(responseCode = "200", description = "Disponibilidad obtenida correctamente.")
	@ApiJsonExample(
			description = "Disponibilidad obtenida correctamente.",
			path = "/static/swagger/examples/planning/get-technician-availability-200.json",
			summary = "Disponibilidad obtenida"
	)
	@GetMapping(value = "/{technicianId}/availability", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TechnicianAvailabilityResponse>> getTechnicianAvailability(
			@PathVariable UUID technicianId,
			@RequestParam OffsetDateTime from,
			@RequestParam OffsetDateTime to
	) {
		List<TechnicianAvailabilityResponse> response = schedulingService
				.getTechnicianAvailability(technicianId, from, to);

		return ResponseEntity.ok(response);
	}

	@Operation(
			summary = "Obtener la agenda de un técnico",
			description = """
					Devuelve la agenda del técnico especificado en el rango de fecha y hora indicado.
					La agenda contendrá las órdenes de trabajo que el técnico tiene asignadas.
					Facilita la planificación de las órdenes de trabajo.
					"""
	)
	@ApiResponse(responseCode = "200", description = "Agenda obtenida correctamente.")
	@ApiJsonExample(
			description = "Agenda obtenida correctamente.",
			path = "/static/swagger/examples/planning/get-technician-agenda-200.json",
			summary = "Agenda obtenida"
	)
	@GetMapping(value = "/{technicianId}/agenda", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TechnicianAgendaResponse>> getTechnicianAgenda(
			@PathVariable UUID technicianId,
			@RequestParam OffsetDateTime from,
			@RequestParam OffsetDateTime to
	) {
		List<TechnicianAgendaResponse> response = schedulingService.getTechnicianAgenda(technicianId, from, to);

		return ResponseEntity.ok(response);
	}
}
