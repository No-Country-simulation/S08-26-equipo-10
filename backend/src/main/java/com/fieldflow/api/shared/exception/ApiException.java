package com.fieldflow.api.shared.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción de negocio. El controlador de excepciones global la traduce al formato
 * RFC 9457 (Problem Details).
 */
public class ApiException extends RuntimeException {

	private final HttpStatus status;
	private final ApiErrorType type;

	public ApiException(HttpStatus status, ApiErrorType type, String message) {
		super(message);
		this.status = status;
		this.type = type;
	}

	/**
	 * Crea una excepción de recurso no encontrado.
	 * <p>
	 * Ejemplos de uso: El recurso solicitado o referenciado no existe.
	 *
	 * @param message el mensaje de error
	 * @return una instancia de ApiException con estado 404 y tipo RESOURCE_NOT_FOUND
	 */
	public static ApiException notFound(String message) {
		return new ApiException(HttpStatus.NOT_FOUND, ApiErrorType.RESOURCE_NOT_FOUND, message);
	}

	/**
	 * Crea una excepción de error de validación.
	 * <p>
	 * Ejemplos de uso: request mal formada, UUID inválido, campo requerido ausente, valor o intervalo básico inválido.
	 *
	 * @param message el mensaje de error
	 * @return una instancia de ApiException con estado 400 y tipo VALIDATION_ERROR
	 */
	public static ApiException badRequest(String message) {
		return new ApiException(HttpStatus.BAD_REQUEST, ApiErrorType.VALIDATION_ERROR, message);
	}

	/**
	 * Crea una excepción de error interno del servidor.
	 * <p>
	 * Ejemplos de uso: error inesperado en la lógica de negocio, fallo en la base de datos, etc.
	 *
	 * @param message el mensaje de error
	 * @return una instancia de ApiException con estado 500 y tipo INTERNAL_ERROR
	 */
	public static ApiException internalError(String message) {
		return new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ApiErrorType.INTERNAL_ERROR, message);
	}

	/**
	 * Crea una excepción de error de solicitud incorrecta con un tipo específico.
	 * <p>
	 * Nota: Esta función permite especificar un tipo de error más detallado para solicitudes incorrectas,
	 * como errores de validación específicos o conflictos de programación.
	 *
	 * @param message el mensaje de error
	 * @param type    el tipo de error
	 * @return una instancia de ApiException con estado 400 y el tipo especificado
	 */
	public static ApiException badRequest(String message, ApiErrorType type) {
		return new ApiException(HttpStatus.BAD_REQUEST, type, message);
	}

	/**
	 * Crea una excepción de conflicto con un tipo específico.
	 * <p>
	 * Nota: Esta función permite especificar un tipo de error más detallado para conflictos,
	 * como conflictos de datos o conflictos de lógica de negocio.
	 *
	 * @param message el mensaje de error
	 * @param type    el tipo de error
	 * @return una instancia de ApiException con estado 409 y el tipo especificado
	 */
	public static ApiException conflict(String message, ApiErrorType type) {
		return new ApiException(HttpStatus.CONFLICT, type, message);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public ApiErrorType getType() {
		return type;
	}
}
