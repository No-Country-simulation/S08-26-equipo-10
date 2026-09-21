package com.fieldflow.workorders.api.dto;

import com.fieldflow.workorders.domain.WorkOrderStatus;
import jakarta.validation.constraints.NotNull;

public record WorkOrderStatusUpdateRequest(
		@NotNull(message = "Se debe especificar el estado de la Orden de Trabajo")
		WorkOrderStatus status
) {
}
