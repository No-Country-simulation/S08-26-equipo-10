package com.fieldflow.workorders.api;

import com.fieldflow.shared.annotations.ApiJsonExample;
import com.fieldflow.workorders.api.dto.WorkOrderDetailResponse;
import com.fieldflow.workorders.api.dto.WorkOrderSummaryResponse;
import com.fieldflow.workorders.application.WorkOrderService;
import com.fieldflow.workorders.domain.WorkOrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
					"""
	)
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
}
