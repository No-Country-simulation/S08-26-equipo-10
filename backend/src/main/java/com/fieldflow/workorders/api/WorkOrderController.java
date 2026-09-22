package com.fieldflow.workorders.api;

import com.fieldflow.planning.api.dto.AssignmentDetailResponse;
import com.fieldflow.planning.api.dto.AssignmentRequest;
import com.fieldflow.shared.annotations.ApiJsonExample;
import com.fieldflow.workorders.api.dto.*;
import com.fieldflow.workorders.application.WorkOrderService;
import com.fieldflow.workorders.domain.WorkOrderStatus;
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
@RequestMapping("/api/v1/work-orders")
@Tag(name = "Órdenes de Trabajo", description = "Operaciones relacionadas con las órdenes de trabajo")
public class WorkOrderController {

	private final WorkOrderService workOrderService;

	public WorkOrderController(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	@Operation(
			summary = "Obtener todas las órdenes de trabajo",
			description = """
					Obtiene una lista de todas las órdenes de trabajo,
					con la opción de filtrar por estado y/o ID del equipo.
					"""
	)
	@ApiResponse(responseCode = "200", description = "Lista de órdenes de trabajo obtenida exitosamente.")
	@ApiJsonExample(
			description = "Ejemplo de respuesta para obtener todas las órdenes de trabajo",
			path = "/static/swagger/examples/workorders/list-work-orders-200.json",
			summary = "Lista de órdenes de trabajo"
	)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<WorkOrderSummaryResponse>> getAllWorkOrders(
			@RequestParam(required = false) WorkOrderStatus status,
			@RequestParam(required = false) UUID equipmentId) {
		List<WorkOrderSummaryResponse> response = workOrderService.getAllWorkOrders(status, equipmentId);
		return ResponseEntity.ok(response);
	}

	@Operation(
			summary = "Obtener detalles de una orden de trabajo",
			description = """
					Obtiene los detalles de una orden de trabajo específica buscando por su ID.
					`assignment` puede ser null, `checklist` puede ser null, `installation` puede ser null,
					`interventions` puede estar vacío y `conformity` puede ser null.
					Una orden de trabajo (OT) puede tener múltiples intervenciones.
					"""
	)
	@ApiResponse(responseCode = "200", description = "Detalles de la orden de trabajo obtenido exitosamente.")
	@ApiJsonExample(
			description = "Ejemplo de respuesta para obtener detalles de una orden de trabajo",
			path = "/static/swagger/examples/workorders/get-work-order-detail-200.json",
			summary = "Detalles de la orden de trabajo"
	)
	@GetMapping(path = "/{workOrderId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WorkOrderDetailResponse> getWorkOrderDetail(@PathVariable UUID workOrderId) {
		WorkOrderDetailResponse response = workOrderService.getWorkOrderDetailById(workOrderId);
		return ResponseEntity.ok(response);
	}

	@Operation(
			summary = "Crear una nueva orden de trabajo",
			description = """
					Crea una nueva orden de trabajo con los datos proporcionados.
					Devuelve una respuesta con el ID de la orden de trabajo creada.
					"""
	)
	@ApiResponse(responseCode = "201", description = "Orden de trabajo creada exitosamente.")
	@ApiJsonExample(
			status = "201",
			description = "Ejemplo de respuesta al crear una nueva orden de trabajo",
			path = "/static/swagger/examples/workorders/create-work-order-201.json",
			summary = "Orden de trabajo creada"
	)
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WorkOrderSummaryResponse> createWorkOrder(@Valid @RequestBody CreateWorkOrderRequest request) {
		WorkOrderSummaryResponse response = workOrderService.createWorkOrder(request);

		URI location = fromMethodCall(on(WorkOrderController.class).getWorkOrderDetail(response.id())).build().toUri();

		return ResponseEntity.created(location).body(response);
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
		var response = workOrderService.assignWorkOrder(workOrderId, request);
		return ResponseEntity.ok(response);
	}

	@Operation(
			summary = "Actualiza el estado de una orden de trabajo",
			description = """
					Permite actualizar el estado de una orden de trabajo.
					Solo se permiten 2 estados: `EN_ROUTE` o `RESCHEDULED`.
					"""
	)
	@ApiResponse(responseCode = "200", description = "El estado de la orden de trabajo se actualizó correctamente")
	@ApiJsonExample(
			description = "Ejemplo de actualización de estado de una orden de trabajo",
			path = "/static/swagger/examples/workorders/work-order-status-update-200.json",
			summary = "Orden de trabajo con estado actualizado"
	)
	@PatchMapping(value = "/{workOrderId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WorkOrderStatusResponse> assignWorkOrder(
			@PathVariable("workOrderId") UUID workOrderId, @Valid @RequestBody WorkOrderStatusUpdateRequest request
	) {
		var response = workOrderService.updateStatus(workOrderId, request);
		return ResponseEntity.ok(response);
	}
}
