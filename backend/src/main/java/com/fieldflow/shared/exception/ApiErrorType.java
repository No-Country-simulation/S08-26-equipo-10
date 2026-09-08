package com.fieldflow.shared.exception;

/**
 * Tipos de errores que pueden ocurrir en la API.
 */
public enum ApiErrorType {
	VALIDATION_ERROR(
			"VALIDATION_ERROR",
			"urn:fieldflow:error:validation",
			"Datos de entrada inválidos"
	),
	RESOURCE_NOT_FOUND(
			"RESOURCE_NOT_FOUND",
			"urn:fieldflow:error:not-found",
			"Recurso inexistente"
	),
	TECHNICIAN_NOT_AVAILABLE(
			"TECHNICIAN_NOT_AVAILABLE",
			"urn:fieldflow:error:technician-not-available",
			"Técnico no disponible"
	),
	SCHEDULE_OVERLAP(
			"SCHEDULE_OVERLAP",
			"urn:fieldflow:error:schedule-overlap",
			"Solapamiento de horarios"
	),
	CHECKLIST_ALREADY_EXISTS(
			"CHECKLIST_ALREADY_EXISTS",
			"urn:fieldflow:error:checklist-already-exists",
			"Checklist ya existe"
	),
	CONFORMITY_ALREADY_EXISTS(
			"CONFORMITY_ALREADY_EXISTS",
			"urn:fieldflow:error:conformity-already-exists",
			"Conformidad ya existe"
	),
	DUPLICATE_CHECKLIST_RESPONSE(
			"DUPLICATE_CHECKLIST_RESPONSE",
			"urn:fieldflow:error:duplicate-checklist-response",
			"Respuesta de checklist duplicada"
	),
	INVALID_STATUS_TRANSITION(
			"INVALID_STATUS_TRANSITION",
			"urn:fieldflow:error:invalid-status-transition",
			"Transición de estado inválida"
	),
	TECHNICIAN_NOT_ASSIGNED(
			"TECHNICIAN_NOT_ASSIGNED",
			"urn:fieldflow:error:technician-not-assigned",
			"Técnico no asignado"
	),
	CHECKLIST_ITEM_NOT_IN_WORK_ORDER(
			"CHECKLIST_ITEM_NOT_IN_WORK_ORDER",
			"urn:fieldflow:error:checklist-item-not-in-work-order",
			"Elemento de checklist no está en orden de trabajo"
	),
	INTERNAL_ERROR(
			"INTERNAL_ERROR",
			"urn:fieldflow:error:internal-error",
			"Error interno del servidor"
	);

	private final String code;
	private final String uri;
	private final String title;

	ApiErrorType(String code, String uri, String title) {
		this.code = code;
		this.uri = uri;
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public String getUri() {
		return uri;
	}

	public String getTitle() {
		return title;
	}
}
