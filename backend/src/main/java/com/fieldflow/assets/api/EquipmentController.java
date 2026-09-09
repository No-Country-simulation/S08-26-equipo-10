package com.fieldflow.assets.api;

import com.fieldflow.assets.api.dto.EquipmentResponse;
import com.fieldflow.assets.application.EquipmentService;
import com.fieldflow.shared.annotations.ApiJsonExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/equipments")
@Tag(name = "Equipos Industriales", description = "Endpoints para la gestión de equipos industriales.")
public class EquipmentController {

	private final EquipmentService equipmentService;

	public EquipmentController(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	@ApiJsonExample(
			description = "Lista de equipos. Si no hay equipos, devuelve un array vacío.",
			path = "/static/swagger/examples/assets/list-equipments-200.json",
			summary = "Lista de equipos"
	)
	@Operation(
			summary = "Obtiene todos los equipos",
			description = """
					Devuelve un listado de todos los equipos, filtrando por `siteId` y `clientId` si se proporcionan.
					Cada equipo incluirá el cliente asociado, sitio al que pertenece e instalación en la que se ubica.
					Los equipos pueden o no tener una instalación asociada, en cuyo caso se representará como `null`.
					Si no hay equipos, devuelve un arreglo vacío.
					"""
	)
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EquipmentResponse>> getAllEquipments(@RequestParam(required = false) UUID siteId,
	                                                                @RequestParam(required = false) UUID clientId) {
		List<EquipmentResponse> equipments = equipmentService.getAllEquipments(siteId, clientId);
		return ResponseEntity.ok(equipments);
	}
}
