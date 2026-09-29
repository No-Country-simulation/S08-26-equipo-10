package com.fieldflow.execution.api;

import com.fieldflow.shared.annotations.ApiJsonExample;
import com.fieldflow.storage.EvidenceUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interventions")
@Tag(name = "Intervenciones", description = "Operaciones relacionadas con las intervenciones")
public class InterventionController {

	private final EvidenceUploadService evidenceUploadService;

	public InterventionController(EvidenceUploadService evidenceUploadService) {
		this.evidenceUploadService = evidenceUploadService;
	}

	/**
	 * Carga una imagen como evidencia para una intervención específica.
	 *
	 * @param interventionId ID de la intervención.
	 * @param file           Archivo de la imagen a cargar.
	 * @return ResponseEntity con la respuesta de la carga.
	 */
	@Operation(
			summary = "Cargar evidencia",
			description = """
					Carga una imagen como evidencia para una intervención específica.
					Se obtiene una referencia a la evidencia cargada.
					La referencia se compone por el path `evidente/`, el `UUID` de la intervención
					y un `UUID` que identifica a la evidencia en el storage.
					"""
	)
	@ApiResponse(responseCode = "201", description = "Evidencia cargada correctamente")
	@ApiJsonExample(
			status = "201",
			description = "Evidencia cargada correctamente.",
			path = "/static/swagger/examples/workorders/upload-work-order-evidence-201.json",
			summary = "Evidencia cargada"
	)
	@PostMapping(
			path = "/{interventionId}/evidence-uploads",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<UploadResponse> uploadPhoto(@PathVariable UUID interventionId,
	                                                  @RequestPart("file") MultipartFile file) {
		String reference = evidenceUploadService.upload(interventionId, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(new UploadResponse(reference));
	}

	/**
	 * Record para representar la respuesta de una carga de evidencia.
	 *
	 * @param reference referencia de la evidencia cargada.
	 */
	public record UploadResponse(
			String reference
	) {
	}
}
