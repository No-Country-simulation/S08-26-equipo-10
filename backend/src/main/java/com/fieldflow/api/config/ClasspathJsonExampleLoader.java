package com.fieldflow.api.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación de {@link JsonExampleLoader} encargada de localizar y cargar los archivos JSON
 * directamente desde el classpath de la aplicación.
 * <p>
 * Gestionada como un componente de Spring, esta clase incorpora una caché en memoria segura
 * para hilos (utilizando {@link ConcurrentHashMap}) para almacenar los objetos {@link JsonNode}
 * previamente procesados. Esta estrategia optimiza significativamente el rendimiento,
 * evitando operaciones redundantes de entrada/salida (I/O) y parseo cuando un mismo archivo
 * JSON de ejemplo es solicitado múltiples veces durante la generación de la documentación.
 * </p>
 */
@Component
public class ClasspathJsonExampleLoader implements JsonExampleLoader {

	private final ObjectMapper objectMapper = new ObjectMapper();

	// Caché en memoria mediante mapa concurrente para almacenar los JSON ya leídos
	private final Map<String, JsonNode> cache = new ConcurrentHashMap<>();

	/**
	 * Recupera el contenido JSON desde la ruta indicada en el classpath.
	 * <p>
	 * Si el archivo ya fue solicitado anteriormente, su contenido se devuelve de manera
	 * inmediata desde la caché en memoria. En caso de ser la primera solicitud para esa ruta,
	 * el archivo se lee, se parsea y se guarda en la caché.
	 * </p>
	 *
	 * @param path La ruta interna en el classpath que apunta al archivo JSON.
	 * @return El objeto {@link JsonNode} con el contenido del archivo parseado.
	 * @throws IllegalArgumentException si el archivo no existe o si ocurre un error de lectura/parseo
	 *                                  ({@link IOException}) durante su procesamiento.
	 */
	@Override
	public JsonNode load(String path) {
		// Retorna el JsonNode de la caché o lee el archivo si es la primera vez que se solicita
		return cache.computeIfAbsent(path, this::readJsonFromClasspath);
	}

	/**
	 * Método auxiliar interno que realiza la lectura física del archivo utilizando
	 * {@link ClassPathResource} y lo convierte en un árbol JSON mediante {@link ObjectMapper}.
	 *
	 * @param path La ruta del archivo en el classpath.
	 * @return El {@link JsonNode} resultante de parsear el InputStream del archivo.
	 */
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
