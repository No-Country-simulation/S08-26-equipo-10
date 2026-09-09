package com.fieldflow.planning.api;

import com.fieldflow.planning.api.dto.TechnicianResponse;
import com.fieldflow.planning.application.TechnicianService;
import com.fieldflow.shared.annotations.ApiJsonExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
	@ApiJsonExample(
			description = "Lista de técnicos presentes en la plataforma.",
			path = "/static/swagger/examples/planning/list-technicians-200.json",
			summary = "Lista de técnicos"
	)
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TechnicianResponse>> getAllTechnicians() {
		List<TechnicianResponse> technicians = technicianService.getAllTechnicians();
		return ResponseEntity.ok(technicians);
	}
}
