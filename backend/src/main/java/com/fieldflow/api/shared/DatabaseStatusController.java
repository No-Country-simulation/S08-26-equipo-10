package com.fieldflow.api.shared;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Estado de la conexión con PostgreSQL.
 * <p>
 * A diferencia de /ping, que solo dice que el proceso responde, este endpoint pide
 * una conexión real al pool y ejecuta una consulta. Es la forma rápida de distinguir
 * "la API esta caída" de "la API está arriba, pero no llega a la base de datos".
 * <p>
 * 200 -> conexión establecida. 503 -> no se pudo conectar.
 * <p>
 * El detalle de infraestructura (host, usuario, pool) solo se incluye si
 * app.db-status.show-details esta en true. El error nunca sale al cliente:
 * va al log y al cliente solo el SQLState.
 */
@RestController
@RequestMapping("/api/v1")
public class DatabaseStatusController {

	private static final Logger log = LoggerFactory.getLogger(DatabaseStatusController.class);

	private final DataSource dataSource;
	private final boolean showDetails;

	public DatabaseStatusController(DataSource dataSource,
	                                @Value("${app.db-status.show-details}") boolean showDetails) {
		this.dataSource = dataSource;
		this.showDetails = showDetails;
	}

	@GetMapping(value = "/db-status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> databaseStatus() {
		Map<String, Object> body = new LinkedHashMap<>();
		long startedAt = System.nanoTime();

		try (Connection connection = dataSource.getConnection();
		     Statement statement = connection.createStatement()) {

			statement.execute("SELECT 1");
			long latencyMs = Duration.ofNanos(System.nanoTime() - startedAt).toMillis();

			body.put("status", "UP");
			body.put("latencyMs", latencyMs);
			if (showDetails) {
				body.putAll(details(connection));
			}
			body.put("timestamp", Instant.now().toString());
			return ResponseEntity.ok(body);

		} catch (SQLException ex) {
			// El detalle completo va al log del servidor; al cliente solo el código
			// estándar SQLState (08001 = no se pudo abrir la conexión, 28P01 = password
			// inválido, 3D000 = la base no existe). Nunca la traza ni el mensaje interno.
			log.error("No se pudo establecer la conexión con la base de datos", ex);

			body.put("status", "DOWN");
			body.put("sqlState", ex.getSQLState());
			body.put("timestamp", Instant.now().toString());
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
		}
	}

	/**
	 * Datos de diagnóstico.
	 */
	private Map<String, Object> details(Connection connection) throws SQLException {
		Map<String, Object> details = new LinkedHashMap<>();
		DatabaseMetaData metaData = connection.getMetaData();

		details.put("database", metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
		details.put("schema", connection.getSchema());
		details.put("readOnly", connection.isReadOnly());

		if (dataSource instanceof HikariDataSource hikari) {
			HikariPoolMXBean pool = hikari.getHikariPoolMXBean();
			if (pool != null) {
				Map<String, Object> poolStats = new LinkedHashMap<>();
				poolStats.put("name", hikari.getPoolName());
				poolStats.put("active", pool.getActiveConnections());
				poolStats.put("idle", pool.getIdleConnections());
				poolStats.put("total", pool.getTotalConnections());
				poolStats.put("waiting", pool.getThreadsAwaitingConnection());
				poolStats.put("max", hikari.getMaximumPoolSize());
				details.put("pool", poolStats);
			}
		}
		return details;
	}
}
