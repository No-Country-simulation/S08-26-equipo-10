# Build multi-etapa
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app/backend

# Copiamos todo el contenido de la carpeta backend
COPY backend/ .

# Corregimos los saltos de línea (CRLF a LF) por si se editó en Windows
RUN chmod +x mvnw && sed -i 's/\r$//' mvnw

# Descargamos dependencias usando el wrapper corregido
RUN ./mvnw dependency:go-offline

# Copiamos las migraciones que viven fuera de backend/
COPY database/ /app/database/

# Compilamos el proyecto
RUN ./mvnw clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S app && adduser -S app -G app
COPY --from=build /app/backend/target/*.jar app.jar
USER app
EXPOSE 8080

ENV JAVA_OPTS="-XX:MaxRAMPercentage=70 -XX:+UseSerialGC"

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]