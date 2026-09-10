package com.fieldflow.workorders.domain;

public enum WorkOrderPriority {

	LOW("Inspección no urgente o tarea menor."),
	MEDIUM("Mantenimiento correctivo normal."),
	HIGH("Equipo degradado con riesgo de interrupción."),
	CRITICAL("Equipo detenido o servicio esencial afectado.");

	private final String description;

	WorkOrderPriority(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
