package com.fieldflow.api.shared.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación contenedora que permite declarar múltiples instancias de {@link ApiJsonExample} sobre un mismo método.
 * <p>
 * Agrupa los distintos escenarios de respuesta (por ejemplo, casos de éxito, errores de validación,
 * recursos no encontrados) trasladándolos a la configuración interna para ser procesados
 * en bloque por la documentación de Swagger/OpenAPI.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiJsonExamples {

	/**
	 * Arreglo de anotaciones de ejemplos JSON que se aplicarán al endpoint.
	 *
	 * @return Arreglo de instancias de {@link ApiJsonExample}.
	 */
	ApiJsonExample[] value();
}
