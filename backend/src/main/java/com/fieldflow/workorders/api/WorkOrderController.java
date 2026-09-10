package com.fieldflow.workorders.api;

import com.fieldflow.shared.annotations.ApiJsonExample;
import com.fieldflow.workorders.api.dto.WorkOrderResponse;
import com.fieldflow.workorders.application.WorkOrderService;
import com.fieldflow.workorders.domain.WorkOrderStatus;
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
	@ApiJsonExample(
			description = "Ejemplo de respuesta para obtener todas las órdenes de trabajo",
			path = "/static/swagger/examples/workorders/list-work-orders-200.json",
			summary = "Lista de órdenes de trabajo"
	)
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<WorkOrderResponse>> getAllWorkOrders(
			@RequestParam(required = false) WorkOrderStatus status,
			@RequestParam(required = false) UUID equipmentId) {
		List<WorkOrderResponse> response = workOrderService.getAllWorkOrders(status, equipmentId);
		return ResponseEntity.ok(response);
	}
}
