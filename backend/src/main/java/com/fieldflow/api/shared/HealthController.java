package com.fieldflow.api.shared;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * Endpoint mínimo para verificar que el despliegue responde.
 * Es lo primero que debe funcionar en staging, antes que cualquier lógica de negocio.
 */
@RestController
@RequestMapping("/api/v1")
public class HealthController {

	@GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HealthResponse> ping() {
		HealthResponse response = new HealthResponse(
				"ok",
				"fieldflow-api",
				Instant.now().toString()
		);
		return ResponseEntity.ok(response);
	}

	public record HealthResponse(
			String status,
			String service,
			String timestamp
	) {
	}
}
