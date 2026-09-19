package com.fieldflow.planning.api;

import com.fieldflow.planning.api.dto.AssignmentRequest;
import com.fieldflow.planning.api.dto.AssignmentDetailResponse;
import com.fieldflow.planning.application.AssignmentService;
import com.fieldflow.shared.annotations.ApiJsonExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/work-orders")
@Tag(name = "Asignaciones", description = "Endpoints para la gestión de asignaciones de órdenes de trabajo")
public class AssignmentController {

	private final AssignmentService assignmentService;

	public AssignmentController(AssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

	@Operation(
			summary = "Asigna una orden de trabajo a un trabajador",
			description = """
					Permite asignar una orden de trabajo a un trabajador específico.
					La orden de trabajo pasa a `ASSIGNED`.
					"""
	)
	@ApiResponse(responseCode = "200", description = "La orden de trabajo se asignó correctamente")
	@ApiJsonExample(
			description = "Ejemplo de asignación de una orden de trabajo",
			path = "/static/swagger/examples/planning/work-order-assignment-200.json",
			summary = "Orden de trabajo asignada"
	)
	@PutMapping(value = "/{workOrderId}/assignment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AssignmentDetailResponse> assignWorkOrder(@PathVariable("workOrderId") UUID workOrderId,
	                                                                @Valid @RequestBody AssignmentRequest request) {
		var response = assignmentService.assignWorkOrder(workOrderId, request);
		return ResponseEntity.ok(response);
	}
}
