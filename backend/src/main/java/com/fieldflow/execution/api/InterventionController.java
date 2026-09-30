package com.fieldflow.execution.api;

import com.fieldflow.execution.api.dto.InterventionReportRequest;
import com.fieldflow.execution.api.dto.InterventionReportResponse;
import com.fieldflow.execution.application.InterventionService;
import com.fieldflow.shared.annotations.ApiJsonExample;
import com.fieldflow.storage.EvidenceUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interventions")
@Tag(name = "Intervenciones", description = "Carga de evidencias y envío de reportes de visitas")
public class InterventionController {

	private final EvidenceUploadService evidenceUploadService;
	private final InterventionService interventionService;

	public InterventionController(EvidenceUploadService evidenceUploadService,
	                              InterventionService interventionService) {
		this.evidenceUploadService = evidenceUploadService;
		this.interventionService = interventionService;
	}

	/**
	 * Carga una imagen como evidencia para una intervención específica.
	 *
	 * @param interventionId ID de la intervención.
	 * @param file           Archivo de la imagen a cargar.
	 * @return ResponseEntity con la respuesta de la carga.
	 */
	@Operation(
			summary = "Subir imagen de evidencia",
			description = """
					Sube una imagen JPEG o PNG de hasta 12 MiB mientras la intervención y su OT están en `IN_PROGRESS`.
					Devuelve una referencia lógica para `evidence[].reference` al enviar el reporte.
					"""
	)
	@ApiResponse(responseCode = "201", description = "Imagen cargada; devuelve su referencia lógica")
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

	@Operation(
			summary = "Enviar reporte de intervención",
			description = """
					Registra en una transacción la fecha de fin, el resultado,
					el checklist y los demás detalles de la visita, incluidas las referencias de evidencias ya subidas.
					Al finalizar, la intervención y la OT pasan a `PENDING_CUSTOMER_CONFIRMATION`.
					"""
	)
	@ApiResponse(responseCode = "200", description = "Reporte registrado; intervención y OT pendientes de confirmación")
	@ApiJsonExample(
			description = "Reporte enviado correctamente.",
			path = "/static/swagger/examples/workorders/update-intervention-report-200.json",
			summary = "Reporte enviado"
	)
	@PutMapping(value = "/{interventionId}/report", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InterventionReportResponse> submitReport(
			@PathVariable UUID interventionId, @RequestBody @Valid InterventionReportRequest request
	) {
		var response = interventionService.submitReport(interventionId, request);
		return ResponseEntity.ok(response);
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
