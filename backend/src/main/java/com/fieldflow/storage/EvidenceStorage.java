package com.fieldflow.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fieldflow.shared.exception.EvidenceFileReadException;
import com.fieldflow.shared.exception.EvidenceStorageException;
import com.fieldflow.shared.exception.InvalidEvidenceFileException;
import com.fieldflow.shared.exception.InvalidEvidenceFileException.Reason;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class EvidenceStorage {

	private static final long MAX_BYTES = 12L * 1024 * 1024;

	private final RestClient client;
	private final String storageUrl;
	private final String bucket;

	public EvidenceStorage(
			RestClient.Builder builder,
			@Value("${app.supabase.url}") String projectUrl,
			@Value("${app.supabase.secret-key}") String secretKey,
			@Value("${app.supabase.bucket-name}") String bucket) {

		this.storageUrl = projectUrl.replaceAll("/+$", "") + "/storage/v1";
		this.bucket = bucket;
		this.client = builder.defaultHeader("apikey", secretKey).build();
	}

	public PreparedImage prepare(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new InvalidEvidenceFileException(Reason.EMPTY, "La imagen está vacía");
		}

		if (file.getSize() > MAX_BYTES) {
			throw new InvalidEvidenceFileException(Reason.TOO_LARGE, "La imagen supera el tamaño máximo permitido");
		}

		byte[] content;
		try {
			content = file.getBytes();
		} catch (IOException ex) {
			throw new EvidenceFileReadException("No se pudo leer la imagen recibida", ex);
		}

		if (content.length == 0) {
			throw new InvalidEvidenceFileException(Reason.EMPTY, "La imagen está vacía");
		}
		if (content.length > MAX_BYTES) {
			throw new InvalidEvidenceFileException(Reason.TOO_LARGE, "La imagen supera el tamaño máximo permitido");
		}

		MediaType mediaType = detectImageType(content);
		String extension = mediaType.equals(MediaType.IMAGE_JPEG) ? "jpg" : "png";
		return new PreparedImage(content, mediaType, extension);
	}

	public String newReference(UUID interventionId, PreparedImage image) {
		return "evidence/" + interventionId + "/" + UUID.randomUUID() + "." + image.extension();
	}

	public void upload(String reference, PreparedImage image) {
		URI uri = URI.create(storageUrl + "/object/" + bucket + "/" + reference);

		try {
			client.post()
					.uri(uri)
					.contentType(image.mediaType())
					.header("x-upsert", "false")
					.body(image.content())
					.retrieve()
					.toBodilessEntity();
		} catch (RestClientException ex) {
			throw new EvidenceStorageException("No se pudo subir la imagen al Storage", ex);
		}
	}

	public URI signedUrl(String reference) {
		URI uri = URI.create(storageUrl + "/object/sign/" + bucket + "/" + reference);

		SignedUrlResponse response;
		try {
			response = client.post()
					.uri(uri)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Map.of("expiresIn", 300))
					.retrieve()
					.body(SignedUrlResponse.class);
		} catch (RestClientException ex) {
			throw new EvidenceStorageException("No se pudo generar la URL firmada", ex);
		}

		if (response == null || response.signedURL() == null || response.signedURL().isBlank()) {
			throw new EvidenceStorageException("Storage devolvió una respuesta inválida");
		}

		return URI.create(storageUrl + response.signedURL());
	}

	private static MediaType detectImageType(byte[] bytes) {
		boolean jpeg = bytes.length >= 3
				&& (bytes[0] & 0xff) == 0xff
				&& (bytes[1] & 0xff) == 0xd8
				&& (bytes[2] & 0xff) == 0xff;

		byte[] pngSignature = {
				(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a
		};
		boolean png = bytes.length >= pngSignature.length
				&& Arrays.equals(
				Arrays.copyOf(bytes, pngSignature.length),
				pngSignature);

		if (jpeg) return MediaType.IMAGE_JPEG;
		if (png) return MediaType.IMAGE_PNG;

		throw new InvalidEvidenceFileException(
				Reason.UNSUPPORTED_TYPE,
				"Solo se admiten imágenes JPEG y PNG");
	}

	private record SignedUrlResponse(
			@JsonProperty("signedURL") String signedURL) {
	}

	public record PreparedImage(
			byte[] content,
			MediaType mediaType,
			String extension
	) {
	}
}
