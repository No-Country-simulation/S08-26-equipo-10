package com.fieldflow.shared;

import com.fieldflow.shared.exception.ApiException;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Validador de UUIDs.
 */
public class UUIDValidator {

	private static final Pattern STRICT_UUID_PATTERN = Pattern.compile(
			"^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
	);

	/**
	 * Valida si un String tiene el formato estricto de un UUID (8-4-4-4-12)
	 *
	 * @param id String a validar
	 * @return true si es un UUID, false en caso contrario
	 */
	public static boolean isValidUUID(String id) {
		if (id == null || id.isBlank() || id.trim().isEmpty() || id.length() != 36) {
			return false; // Descarte rápido sin usar regex
		}
		return STRICT_UUID_PATTERN.matcher(id).matches();
	}

	/**
	 * Parsea de forma segura un String en un UUID. Lanza excepción si el formato es incorrecto.
	 *
	 * @param id String a parsear
	 * @return UUID parseado
	 */
	public static UUID safeParse(String id) {
		if (!isValidUUID(id)) {
			throw ApiException.badRequest("El UUID proporcionado tiene un formato incorrecto: " + id);
		}
		return UUID.fromString(id);
	}
}

