package com.fieldflow.api.shared.annotations;

import java.lang.annotation.*;

/**
 * Anotación personalizada utilizada para documentar ejemplos de respuestas en Swagger/OpenAPI
 * externalizando el contenido.
 * <p>
 * Diseñada para evitar la contaminación visual de los controladores con cadenas JSON extensas.
 * Permite indicar la ruta física o de classpath donde reside el archivo con el ejemplo JSON.
 * Un procesador interno se encarga de leer esta configuración y trasladar el contenido del archivo
 * directamente a la especificación de la API.
 * </p>
 * <p>
 * Esta anotación es aplicable a métodos y es repetible gracias a su contenedor {@link ApiJsonExamples}.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ApiJsonExamples.class)
public @interface ApiJsonExample {

	/**
	 * El código de estado HTTP asociado a este ejemplo de respuesta.
	 *
	 * @return El código de estado (por defecto "200").
	 */
	String status() default "200";

	/**
	 * Descripción detallada del escenario que produce esta respuesta.
	 *
	 * @return La descripción (por defecto "Operación exitosa").
	 */
	String description() default "Operación exitosa";

	/**
	 * Ruta de acceso al archivo que contiene la carga útil (payload) JSON de ejemplo.
	 * Es el único atributo obligatorio.
	 *
	 * @return La ruta del archivo.
	 */
	String path();

	/**
	 * El tipo de medio (MIME type) de la respuesta documentada.
	 *
	 * @return El tipo de medio (por defecto "application/json").
	 */
	String mediaType() default "application/json";

	/**
	 * Un breve resumen o título que identifica rápidamente el propósito del ejemplo.
	 *
	 * @return El resumen del ejemplo (vacío por defecto).
	 */
	String summary() default "";
}
