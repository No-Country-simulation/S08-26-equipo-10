package com.fieldflow.api.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClasspathJsonExampleLoader implements JsonExampleLoader {

	private final ObjectMapper objectMapper = new ObjectMapper();

	// Caché en memoria mediante mapa concurrente para almacenar los JSON ya leídos
	private final Map<String, JsonNode> cache = new ConcurrentHashMap<>();

	@Override
	public JsonNode load(String path) {
		// Retorna el JsonNode de la caché o lee el archivo si es la primera vez que se solicita
		return cache.computeIfAbsent(path, this::readJsonFromClasspath);
	}

	private JsonNode readJsonFromClasspath(String path) {
		try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
			return objectMapper.readTree(inputStream);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"No se pudo cargar o parsear el archivo de ejemplo JSON desde el classpath: " + path, e
			);
		}
	}
}
