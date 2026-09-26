package com.fieldflow.workorders.domain;

public enum WorkOrderStatus {

	PENDING,
	ASSIGNED,
	EN_ROUTE,
	IN_PROGRESS,
	PENDING_CUSTOMER_CONFIRMATION,
	COMPLETED,
	RESCHEDULED;

	public boolean canStartIntervention() {
		return this == ASSIGNED || this == EN_ROUTE || this == IN_PROGRESS;
	}
}
