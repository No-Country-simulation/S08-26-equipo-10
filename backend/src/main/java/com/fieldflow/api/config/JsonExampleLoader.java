package com.fieldflow.api.config;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Interfaz que define el contrato para la carga de archivos de ejemplo en formato JSON.
 * <p>
 * Su propósito es abstraer el mecanismo o el origen desde el cual se recuperan los datos
 * (por ejemplo, desde el classpath, un sistema de archivos externo o una base de datos)
 * para proveer el contenido estructurado a la capa de documentación de la API.
 * </p>
 */
public interface JsonExampleLoader {

	/**
	 * Carga y procesa un archivo JSON a partir de la ruta especificada.
	 *
	 * @param path La ruta o identificador donde se localiza el archivo JSON de ejemplo.
	 * @return Un objeto {@link JsonNode} que contiene la estructura jerárquica del JSON cargado.
	 */
	JsonNode load(String path);
}
