package com.fieldflow.assets.api;

import com.fieldflow.assets.api.dto.EquipmentDetailResponse;
import com.fieldflow.assets.application.EquipmentService;
import com.fieldflow.maintenance.api.dto.CreatePreventiveMaintenancePlanRequest;
import com.fieldflow.maintenance.api.dto.PreventiveMaintenancePlanResponse;
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

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/api/v1/equipments")
@Tag(name = "Equipos Industriales", description = "Endpoints para la gestión de equipos industriales.")
public class EquipmentController {

	private final EquipmentService equipmentService;

	public EquipmentController(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	@Operation(
			summary = "Obtiene todos los equipos",
			description = """
					Devuelve un listado de todos los equipos, filtrando por `siteId` y `clientId` si se proporcionan.
					Cada equipo incluirá el cliente asociado, sitio al que pertenece e instalación en la que se ubica.
					Los equipos pueden o no tener una instalación asociada, en cuyo caso se representará como `null`.
					Si no hay equipos, devuelve un arreglo vacío.
					"""
	)
	@ApiResponse(responseCode = "200", description = "Lista de equipos obtenida correctamente.")
	@ApiJsonExample(
			description = "Lista de equipos. Si no hay equipos, devuelve un array vacío.",
			path = "/static/swagger/examples/assets/list-equipments-200.json",
			summary = "Lista de equipos"
	)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EquipmentDetailResponse>> getAllEquipments(
			@RequestParam(required = false) UUID siteId, @RequestParam(required = false) UUID clientId
	) {
		List<EquipmentDetailResponse> equipments = equipmentService.getAllEquipments(siteId, clientId);
		return ResponseEntity.ok(equipments);
	}

	@Operation(
			summary = "Crea un plan de mantenimiento preventivo",
			description = """
					Crea un plan de mantenimiento preventivo para un equipo específico.
					La recurrencia del plan de mantenimiento preventivo se define en función de la frecuencia
					(día, semana, mes o año) y el intervalo (cantidad de días, semanas, meses o años).
					"""
	)
	@ApiResponse(responseCode = "201", description = "Plan de mantenimiento preventivo creado correctamente.")
	@ApiJsonExample(
			status = "201",
			description = "Ejemplo de plan de mantenimiento preventivo creado correctamente.",
			path = "/static/swagger/examples/assets/create-equipment-maintenance-plan-201.json",
			summary = "Plan de mantenimiento preventivo creado"
	)
	@PostMapping(value = "/{equipmentId}/preventive-maintenance-plans", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PreventiveMaintenancePlanResponse> createMaintenancePlan(
			@PathVariable UUID equipmentId, @Valid @RequestBody CreatePreventiveMaintenancePlanRequest request
	) {
		var response = equipmentService.createPreventiveMaintenancePlan(equipmentId, request);
		URI location = fromMethodCall(on(EquipmentController.class).getMaintenancePlan(response.id())).build().toUri();
		return ResponseEntity.created(location).body(response);
	}

	@Operation(
			summary = "Obtiene los planes de mantenimiento preventivo de un equipo",
			description = """
					Obtiene los planes de mantenimiento preventivo asociados a un equipo específico.
					"""
	)
	@ApiResponse(responseCode = "200", description = "Planes de mantenimiento preventivo obtenidos correctamente.")
	@ApiJsonExample(
			description = "Ejemplo de planes de mantenimiento preventivo obtenidos correctamente.",
			path = "/static/swagger/examples/assets/get-equipment-maintenance-plans-200.json",
			summary = "Planes de mantenimiento preventivo obtenidos"
	)
	@GetMapping(value = "/{equipmentId}/preventive-maintenance-plans", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PreventiveMaintenancePlanResponse>> getMaintenancePlan(@PathVariable UUID equipmentId) {
		var response = equipmentService.getMaintenancePlansByEquipmentId(equipmentId);
		return ResponseEntity.ok(response);
	}
}
