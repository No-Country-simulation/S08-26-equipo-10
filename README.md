# No Country - S08-26-equipo-10

## FieldFlow

FieldFlow es un sistema centralizado para gestionar, planificar, ejecutar y dar trazabilidad a los servicios técnicos realizados en campo.

La plataforma conecta en un único flujo la solicitud de un servicio, la creación de la Orden de Trabajo (OT), la asignación del técnico, la ejecución de la intervención, la recopilación de evidencias, la conformidad del cliente y la consulta del historial de mantenimiento.

## Visión general

Las empresas que brindan servicios técnicos y de mantenimiento atienden clientes con múltiples sedes, instalaciones y equipos. En la operación diaria participan distintas personas y áreas: administración recibe una solicitud o detecta una necesidad, genera una OT, define el tipo de servicio, coordina la agenda y asigna un técnico disponible.

Los técnicos trabajan fuera de la oficina y necesitan acceder desde el campo a la información necesaria para realizar cada trabajo correctamente. También deben poder registrar lo ocurrido durante la intervención sin depender de planillas, mensajes o llamadas que luego deban consolidarse manualmente.

FieldFlow busca convertirse en la fuente única de información para esta operación. De esta manera, el área administrativa puede planificar y hacer seguimiento, mientras que los técnicos pueden consultar el contexto del trabajo, documentar la intervención y generar evidencia desde el lugar del servicio.

## Problema del negocio

La información de una intervención suele quedar distribuida entre planillas, sistemas administrativos, conversaciones y registros individuales de los técnicos. Esta dispersión provoca:

- Falta de trazabilidad desde la solicitud hasta la finalización del servicio.
- Dificultad para consultar en un solo lugar el cliente, la sede, el equipo y las instrucciones de trabajo.
- Asignaciones poco eficientes por falta de visibilidad sobre disponibilidad, agenda, ubicación, prioridad y duración estimada.
- Seguimiento impreciso de los estados reales de las órdenes de trabajo.
- Pérdida o asociación incorrecta de fotografías, notas, mediciones, checklists y firmas.
- Dificultad para reconstruir el historial de fallas, reparaciones y componentes intervenidos de cada equipo.
- Riesgo de olvidar mantenimientos preventivos, incumplir fechas o perder la continuidad de las recurrencias.

El desafío no es solamente coordinar técnicos. Es conectar la planificación con lo que realmente ocurre en campo y conservar evidencia confiable de cada intervención.

## Oportunidad

FieldFlow permitirá transformar la operación actual en un proceso digital, centralizado y trazable. El sistema debe mantener la información asociada durante todo el ciclo:

**Cliente -> Sede -> Equipo -> Orden de Trabajo -> Asignación -> Agenda -> Intervención -> Evidencia -> Conformidad -> Historial**

El resultado esperado es que administración y los técnicos trabajen sobre la misma información, con mayor visibilidad del estado operativo y con un registro completo de cada servicio.

## Alcance funcional

La solución contempla la gestión de:

- Clientes, sedes e instalaciones.
- Equipos y su historial de mantenimiento.
- Órdenes de Trabajo y tipos de servicio.
- Técnicos, disponibilidad y asignaciones.
- Agenda de trabajo, prioridades y fechas previstas.
- Estados de las intervenciones.
- Checklists, mediciones, observaciones y notas técnicas.
- Fotografías y demás evidencias del trabajo realizado.
- Firma o conformidad del cliente.
- Mantenimientos preventivos y recurrencias.

### Estados de una intervención

El seguimiento operativo debe contemplar, como mínimo, los estados:

**Pendiente -> Asignada -> En camino -> En ejecución -> Pendiente de conformidad -> Finalizada**

También debe permitir marcar una orden como **Reprogramada** cuando un cambio o imprevisto requiera modificar la agenda.

## Flujo principal

1. Administración recibe una solicitud o identifica una necesidad de mantenimiento.
2. Se crea una Orden de Trabajo con el cliente, la sede, el equipo, el tipo de servicio, la prioridad y las instrucciones necesarias.
3. Se revisan la disponibilidad, la agenda, la ubicación y las capacidades de los técnicos.
4. Se asigna el trabajo y se agenda la intervención.
5. El técnico consulta la información de la OT desde el campo y actualiza el estado del servicio.
6. Durante la intervención, registra tareas ejecutadas, checklist, mediciones, notas y fotografías.
7. El cliente confirma la atención mediante firma o conformidad.
8. La OT queda finalizada y sus datos pasan a formar parte del historial del equipo.
9. En el caso de mantenimientos preventivos, el sistema conserva la recurrencia y permite planificar futuras intervenciones.

## Criterio de éxito

El proyecto será exitoso si un usuario puede tomar una Orden de Trabajo y, sin buscar información en diferentes sistemas o canales, conocer:

- Qué cliente solicitó el servicio.
- En qué sede se encuentra el equipo.
- Qué trabajo debía realizarse.
- Qué técnico fue asignado.
- Cuál es el estado actual del servicio.
- Qué se hizo, cuándo se realizó y qué observaciones surgieron.
- Qué checklist, fotografías, notas y conformidad existen.
- Cuál es el historial de mantenimiento del equipo.

## Integrantes del equipo

| Nombre | Rol | Herramientas | Skills |
|---|---|---|---|
| Jesus Luis Toledo | QA Tester | Postman, Visual Studio Code, Selenium, MySQL | Testing manual, Testing de API, Testing Automatizado, QA |
| Nicolas Piazzetti | Software Engineer | Antigravity IDE, Visual Studio Code, Unity, JavaScript, SQL Server | Clean Code, Game Developer, Gestión de proyectos, Testing, Arquitectura de software |
| Héctor Armando Cortez | Backend Developer | Java, SpringBoot, PostgreSQL, MySQL, Docker | Testing REST API, Control de versiones, Arquitectura de software y patrones de diseño, Microservices & monoliths, Observability |
| Paulina Aranda | Backend Developer | Java, Python, PHP, Visual Studio Code | Clean Code, Cloud basics, Prompt engineering |
| Pamela Choy | Full Stack Developer | Java, JavaScript, SQL Server, React, Vue.js | Arquitectura de software y patrones de diseño, Design Systems, Design thinking, Responsive Design, REST API |
| Dangello Galvis | Frontend Developer | React, Node.js, JavaScript | Diseño visual, API integrations, Web3, UX Writing |

## Estructura del repositorio

El proyecto está organizado como un monorepo: un único repositorio de Git con un directorio para cada área de trabajo.

| Directorio | Contenido |
|---|---|
| [`backend/`](backend/) | Código fuente del servicio backend, lógica de negocio, APIs e integraciones. |
| [`frontend/`](frontend/) | Código fuente de la interfaz web, vistas, componentes y estilos. |
| [`database/`](database/) | Migraciones, seeders y recursos de la base de datos. |
| [`docs/`](docs/) | Documentación Markdown, contrato de API, stack y decisiones técnicas. |
| [`design/`](design/) | Diseños UX/UI, wireframes, flujos y recursos visuales. |
| [`testing/`](testing/) | Casos de prueba, evidencias, reportes y validaciones de QA. |

Cada directorio cuenta inicialmente con un README que describe el contenido que alojará. Estos archivos pueden eliminarse cuando comience el desarrollo o conservarse como contexto.

## Stack y herramientas

El stack definitivo será acordado por el equipo y documentado en [`docs/`](docs/). Las herramientas declaradas por los integrantes incluyen Java, SpringBoot, Python, PHP, JavaScript, React, Vue.js, Node.js, PostgreSQL, MySQL, SQL Server, Docker, Postman, Selenium, Visual Studio Code, Antigravity IDE y Unity.

Las decisiones de arquitectura, framework, persistencia y despliegue deberán registrarse junto con sus motivos para facilitar la colaboración entre backend, frontend y QA.
