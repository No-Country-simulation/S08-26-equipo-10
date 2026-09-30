package com.fieldflow.shared;

import com.fieldflow.shared.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Un único formato de error para toda la API (RFC 9457).
 * <p>
 * Regla:
 * - No exponer stack traces ni detalles internos al cliente.
 * - Los errores de entrada utilizan VALIDATION_ERROR.
 * - Los recursos inexistentes utilizan RESOURCE_NOT_FOUND.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log =
			LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * Errores producidos por Jakarta Bean Validation.
	 * <p>
	 * Ejemplos:
	 * - @NotNull
	 * - @NotBlank
	 * - @Positive
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<Map<String, String>> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(fieldError -> Map.of(
						"field", fieldError.getField(),
						"message", String.valueOf(fieldError.getDefaultMessage())
				)).toList();

		String detail = "Uno o más campos no cumplen el contrato.";
		return buildValidationProblem(request, detail, errors);
	}

	/**
	 * Errores producidos durante la deserialización del JSON.
	 * <p>
	 * Ejemplos:
	 * - UUID con formato inválido.
	 * - Enum con valor inexistente.
	 * - Tipo de dato incorrecto.
	 * - JSON sintácticamente inválido.
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
		log.debug("Error al deserializar el cuerpo de la solicitud: {}", ex.getMessage());

		InvalidFormatException invalidFormat = findCause(ex, InvalidFormatException.class);
		if (invalidFormat != null) {
			String field = extractFieldName(invalidFormat);
			Class<?> targetType = invalidFormat.getTargetType();
			String message = resolveInvalidFormatMessage(targetType);

			String detail = "Uno o más campos no cumplen el contrato.";
			Map<String, String> errors = Map.of(
					"field", field,
					"message", message
			);
			return buildValidationProblem(request, detail, List.of(errors));
		}

		/*
		 * Si no existe InvalidFormatException, normalmente se trata
		 * de JSON inválido sintácticamente o de una estructura que
		 * Jackson no pudo interpretar.
		 */
		String detail = "El cuerpo de la solicitud contiene JSON inválido.";
		return buildValidationProblem(request, detail, List.of());
	}

	/**
	 * Errores de conversión en path variables o query parameters.
	 * <p>
	 * Ejemplos:
	 * <p>
	 * /work-orders/not-a-uuid
	 * <p>
	 * ?equipmentId=not-a-uuid
	 * <p>
	 * ?status=INVALID
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
		String field = ex.getName();
		Class<?> requiredType = ex.getRequiredType();
		String message = resolveInvalidFormatMessage(requiredType);

		String detail = "Uno o más parámetros no cumplen el contrato.";
		Map<String, String> errors = Map.of(
				"field", field,
				"message", message
		);
		return buildValidationProblem(request, detail, List.of(errors));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ProblemDetail handleMaxUploadSize(MaxUploadSizeExceededException ex, HttpServletRequest request) {
		log.debug("Solicitud multipart demasiado grande: {}", ex.getMessage());
		return buildUploadTooLargeProblem(request);
	}

	/**
	 * Errores de validación del archivo detectados en EvidenceStorage.
	 */
	@ExceptionHandler(InvalidEvidenceFileException.class)
	public ProblemDetail handleInvalidEvidenceFile(InvalidEvidenceFileException ex, HttpServletRequest request) {
		return switch (ex.getReason()) {
			case EMPTY -> buildValidationProblem(request, ex.getMessage(), List.of());
			case TOO_LARGE -> buildUploadTooLargeProblem(request);
			case UNSUPPORTED_TYPE -> {
				ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
				problem.setType(URI.create("urn:fieldflow:error:unsupported-media-type"));
				problem.setTitle("Formato de archivo no admitido");
				problem.setDetail(ex.getMessage());
				problem.setInstance(URI.create(request.getRequestURI()));
				problem.setProperty("code", "UNSUPPORTED_MEDIA_TYPE");
				yield problem;
			}
		};
	}

	/**
	 * Fallo al leer el archivo recibido; el detalle técnico queda únicamente en los logs.
	 */
	@ExceptionHandler(EvidenceFileReadException.class)
	public ProblemDetail handleEvidenceFileRead(EvidenceFileReadException ex, HttpServletRequest request) {
		String traceId = resolveTraceId(request);
		log.error("[TraceID: {}] No se pudo leer la imagen recibida en {}", traceId, request.getRequestURI(), ex);

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		problem.setType(URI.create(ApiErrorType.INTERNAL_ERROR.getUri()));
		problem.setTitle(ApiErrorType.INTERNAL_ERROR.getTitle());
		problem.setDetail("No se pudo procesar la imagen recibida. Contacte al soporte con el TraceID: " + traceId);
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setProperty("code", ApiErrorType.INTERNAL_ERROR.getCode());
		return problem;
	}

	/**
	 * Fallo del servicio externo de almacenamiento o respuesta inválida de este.
	 */
	@ExceptionHandler(EvidenceStorageException.class)
	public ProblemDetail handleEvidenceStorage(EvidenceStorageException ex, HttpServletRequest request) {
		String traceId = resolveTraceId(request);
		log.error("[TraceID: {}] Error del almacenamiento de evidencias en {}", traceId, request.getRequestURI(), ex);

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
		problem.setType(URI.create("urn:fieldflow:error:evidence-storage-error"));
		problem.setTitle("Error del almacenamiento de evidencias");
		problem.setDetail("No se pudo completar la operación con el almacenamiento de evidencias. TraceID: " + traceId);
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setProperty("code", "EVIDENCE_STORAGE_ERROR");
		return problem;
	}

	private ProblemDetail buildUploadTooLargeProblem(HttpServletRequest request) {
		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONTENT_TOO_LARGE);
		problem.setType(URI.create("urn:fieldflow:error:upload-too-large"));
		problem.setTitle("Archivo demasiado grande");
		problem.setDetail("La imagen no puede superar 12 MiB.");
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setProperty("code", "UPLOAD_TOO_LARGE");
		return problem;
	}

	/**
	 * Recurso HTTP inexistente.
	 * <p>
	 * Por ejemplo, una ruta que no existe.
	 */
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

	/**
	 * Excepciones funcionales controladas por FieldFlow.
	 * <p>
	 * Ejemplos:
	 * - RESOURCE_NOT_FOUND
	 * - SCHEDULE_OVERLAP
	 * - INVALID_STATUS_TRANSITION
	 */
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

	/**
	 * Maneja excepciones de parámetros de solicitud obligatorios faltantes.
	 * No afecta a parámetros opcionales.
	 * <p>
	 * Ejemplo:
	 * <p>
	 * ?equipmentId=1234567890
	 * <p>
	 * ?status=ACTIVE
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ProblemDetail handleMissingRequestParameter(MissingServletRequestParameterException ex,
	                                                   HttpServletRequest request) {
		String detail = "Uno o más parámetros requeridos no fueron enviados.";

		Map<String, String> error = Map.of(
				"field", ex.getParameterName(),
				"message", "es obligatorio"
		);

		return buildValidationProblem(request, detail, List.of(error));
	}

	/**
	 * Cualquier excepción no contemplada.
	 * <p>
	 * Nunca devuelve detalles internos al cliente.
	 */
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest request) {
		String traceId = resolveTraceId(request);

		log.error("[TraceID: {}] Error no controlado en la ruta {}: ", traceId, request.getRequestURI(), ex);

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		problem.setType(URI.create("urn:fieldflow:error:internal-error"));
		problem.setTitle("Error interno del servidor");
		problem.setDetail("Ocurrió un error inesperado. Por favor, contacte al soporte con el TraceID: " + traceId);
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setProperty("code", ApiErrorType.INTERNAL_ERROR.getCode());
		return problem;
	}

	private String resolveTraceId(HttpServletRequest request) {
		return Optional.ofNullable(MDC.get("traceId"))
				.orElseGet(() -> Optional.ofNullable(request.getHeader("X-Trace-Id"))
						.orElseGet(() -> UUID.randomUUID().toString().substring(0, 8)));
	}

	/**
	 * Construye el formato común para errores de validación.
	 */
	private ProblemDetail buildValidationProblem(HttpServletRequest request, String detail,
	                                             List<Map<String, String>> errors) {

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

		problem.setType(URI.create("urn:fieldflow:error:validation"));
		problem.setTitle("Datos de entrada inválidos");
		problem.setDetail(detail);
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setProperty("code", ApiErrorType.VALIDATION_ERROR.getCode());

		if (errors != null && !errors.isEmpty()) {
			problem.setProperty("errors", errors);
		}

		return problem;
	}

	/**
	 * Obtiene el nombre del campo que produjo el error de deserialización de Jackson.
	 */
	private String extractFieldName(InvalidFormatException ex) {
		return ex.getPath()
				.stream()
				.map(JacksonException.Reference::getPropertyName)
				.filter(Objects::nonNull)
				.reduce((first, second) -> second)
				.orElse("body");
	}

	/**
	 * Genera un mensaje adecuado en función del tipo que Jackson/Spring esperaba recibir.
	 */
	private String resolveInvalidFormatMessage(Class<?> targetType) {
		if (targetType == null) {
			return "el valor posee un formato o tipo inválido";
		}

		if (UUID.class.equals(targetType)) {
			return "debe ser un UUID válido";
		}

		if (targetType.isEnum()) {
			String allowedValues = Arrays.stream(targetType.getEnumConstants())
					.map(Object::toString)
					.collect(Collectors.joining(", "));

			return "valor inválido. Valores permitidos: " + allowedValues;
		}

		return "el valor posee un formato o tipo inválido";
	}

	/**
	 * Busca una causa concreta dentro de la cadena de excepciones.
	 */
	private <T extends Throwable> T findCause(Throwable throwable, Class<T> expectedType) {
		Throwable current = throwable;

		while (current != null) {
			if (expectedType.isInstance(current)) {
				return expectedType.cast(current);
			}
			current = current.getCause();
		}

		return null;
	}
}
