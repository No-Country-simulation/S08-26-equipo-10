package com.fieldflow.api.shared;

import com.fieldflow.api.shared.exception.ApiErrorType;
import com.fieldflow.api.shared.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Un único formato de error para toda la API (RFC 9457).
 * Regla: nunca exponer trazas ni mensajes internos al cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> Map.of(
						"field", fieldError.getField(),
						"message", String.valueOf(fieldError.getDefaultMessage())))
				.toList();

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problem.setType(URI.create("urn:fieldflow:error:validation"));
		problem.setTitle("Datos de entrada inválidos");
		problem.setDetail("Uno o más campos no cumplen el contrato.");
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setProperty("code", ApiErrorType.VALIDATION_ERROR.getCode());
		problem.setProperty("errors", errors);
		return problem;
	}

	@ExceptionHandler({
			MethodArgumentTypeMismatchException.class,
			HttpMessageNotReadableException.class,
			IllegalArgumentException.class
	})
	public ProblemDetail handleMalformedRequest(Exception ex, HttpServletRequest request) {
		log.debug("Error de entrada mal formada: {}", ex.getMessage());

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problem.setType(URI.create("urn:fieldflow:error:malformed-request"));
		problem.setTitle("Datos de entrada inválidos");
		problem.setDetail("El formato o tipo de un parámetro o campo del cuerpo de la solicitud es invalido.");
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setProperty("code", "MALFORMED_REQUEST");
		return problem;
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ProblemDetail handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
		log.debug("Recurso no encontrado: {}", ex.getMessage());

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
		problem.setType(URI.create("urn:fieldflow:error:not-found"));
		problem.setTitle("Recurso inexistente");
		problem.setDetail(ex.getMessage());
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setProperty("code", ApiErrorType.RESOURCE_NOT_FOUND.getCode());
		return problem;
	}

	@ExceptionHandler(ApiException.class)
	public ProblemDetail handleApi(ApiException ex, HttpServletRequest request) {
		ProblemDetail problem = ProblemDetail.forStatus(ex.getStatus());
		problem.setType(URI.create(ex.getType().getUri()));
		problem.setTitle(ex.getType().getTitle());
		problem.setDetail(ex.getMessage());
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setProperty("code", ex.getType().getCode());
		return problem;
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest request) {
		String traceId = Optional.ofNullable(MDC.get("traceId"))
				.orElseGet(() -> Optional.ofNullable(request.getHeader("X-Trace-Id"))
						.orElseGet(() -> UUID.randomUUID().toString().substring(0, 8)));
		log.error("[TraceID: {}] Error no controlado en la ruta {}: ", traceId, request.getRequestURI(), ex);
		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		problem.setType(URI.create("urn:fieldflow:error:internal-error"));
		problem.setTitle("Error interno del servidor");
		problem.setDetail("Ocurrió un error inesperado. Por favor, contacte al soporte con el TraceID: " + traceId);
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setProperty("code", ApiErrorType.INTERNAL_ERROR.getCode());
		return problem;
	}
}
