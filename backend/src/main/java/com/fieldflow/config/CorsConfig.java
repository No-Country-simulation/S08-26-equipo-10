package com.fieldflow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Orígenes permitidos explícitos. Nunca "*" cuando se envían credenciales.
 * Se configura con la variable de entorno CORS_ALLOWED_ORIGINS.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Value("${app.cors.allowed-origins}")
	private String[] allowedOrigins;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
				.allowedOrigins(allowedOrigins)
				.allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				// El cliente del frontend envía las peticiones con credentials: "include",
				// asi que el navegador descarta la respuesta si falta esta cabecera.
				.allowCredentials(true)
				.maxAge(3600);
	}
}
