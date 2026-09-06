# FieldFlow — Contrato de API del MVP

**Este documento es el contrato entre el backend y los clientes que consuman la API de FieldFlow durante el MVP.** Define las rutas, estructuras JSON, reglas funcionales, códigos HTTP y convenciones mínimas necesarias para implementar y demostrar el flujo operativo acordado.

- **Versión:** `v1`
- **Estado:** contrato MVP
- **Base path:** `/api/v1`
- **Base URL local:** `http://localhost:8080/api/v1`
- **Base URL staging:** `https://fieldflow-api-2lfo.onrender.com`
- **OpenAPI / Swagger:** la especificación generada desde el código debe reflejar este contrato; su ubicación concreta queda definida por la configuración del backend.

> Este archivo representa el **acuerdo funcional y HTTP**. La especificación OpenAPI generada desde el código representa su **implementación ejecutable**. Si ambos difieren, existe una inconsistencia que debe corregirse antes de considerar terminado el endpoint.

---

## 1. Objetivo, fuentes y gobierno del contrato

### 1.1 Objetivo

Este documento define el contrato HTTP mínimo necesario para implementar el MVP de FieldFlow durante las cuatro semanas restantes de desarrollo.

El criterio rector es **proteger el MVP**. La API no replica cada tabla como un CRUD independiente: expone únicamente los casos de uso necesarios para completar el flujo operativo y demostrar trazabilidad de punta a punta.

Flujo principal soportado:

`Equipo -> Orden de Trabajo -> Asignación -> Agenda -> Intervención -> Evidencia -> Conformidad -> Historial`

Los datos de cliente, sede e instalación se obtienen a través del equipo, evitando duplicarlos en la Orden de Trabajo.

### 1.2 Fuentes de verdad

El contrato se deriva exclusivamente de:

- [`project-description.md`](project-description.md) — fuente de verdad funcional.
- [`modules-and-entities.md`](modules-and-entities.md) — módulos, entidades, relaciones y límites del MVP.
- [`V1__initial_schema.sql`](../database/migrations/V1__initial_schema.sql) — esquema físico PostgreSQL/Supabase y restricciones ya definidas.

Si aparece una necesidad que no puede justificarse a partir de estas fuentes y no es necesaria para ejecutar el escenario de aceptación global definido al final de este documento, debe considerarse **post-MVP**.

### 1.3 Cómo cambiar este contrato

Una modificación del contrato debe realizarse antes o junto con la implementación correspondiente.

Proceso mínimo:

1. Modificar `API.md` mediante un Pull Request.
2. Explicar qué endpoint cambia y por qué.
3. Validar el cambio con quien implemente el backend y con quien consuma el endpoint.
4. Actualizar la implementación y la especificación OpenAPI en el mismo cambio o inmediatamente después.
5. No incorporar nuevas rutas durante el MVP si no son necesarias para completar el flujo principal.

### 1.4 Compatibilidad de cambios

Se consideran **cambios incompatibles** dentro de `v1`:

- eliminar un endpoint;
- cambiar el método HTTP de un endpoint;
- renombrar una ruta o un path parameter;
- eliminar o renombrar un campo de request/response;
- cambiar el tipo de un campo;
- convertir un campo opcional en obligatorio;
- cambiar el significado funcional de un campo;
- cambiar un código HTTP exitoso o de error ya acordado;
- cambiar una transición de estado definida en este contrato.

Se consideran **cambios compatibles**, siempre que no alteren el flujo existente:

- agregar un campo opcional;
- ampliar un detalle de error sin eliminar campos existentes;
- documentar de forma más precisa una regla ya vigente.

Agregar un endpoint nuevo no rompe técnicamente `v1`, pero durante las cuatro semanas restantes requiere además justificar que es imprescindible para el MVP.

---

## 2. Decisiones de alcance para proteger el MVP

### 2.1 Incluido

La API permite:

1. Consultar equipos con su cliente, sede e instalación opcional.
2. Consultar tipos de servicio y técnicos existentes.
3. Registrar y consultar disponibilidad de técnicos.
4. Crear y consultar Órdenes de Trabajo.
5. Asignar o reprogramar una Orden de Trabajo.
6. Consultar la agenda de un técnico a partir de `assignment`.
7. Registrar los estados operativos que no se producen automáticamente por otra acción.
8. Asociar un checklist a una Orden de Trabajo.
9. Iniciar una intervención.
10. Registrar en una única operación el reporte de campo: resultado, observaciones, fallas, reparaciones, componentes, checklist, notas y evidencia.
11. Registrar conformidad del cliente.
12. Consultar el historial de mantenimiento de un equipo.
13. Crear y consultar un plan de mantenimiento preventivo con recurrencia simple.

### 2.2 Deliberadamente fuera del contrato del MVP

No se exponen rutas ni capacidades adicionales para:

- usuarios, roles, permisos o autenticación propia;
- auditoría transversal;
- soft-delete;
- historial independiente de estados de OT;
- entidad o CRUD de agenda;
- historial persistente independiente de mantenimiento;
- catálogo de componentes;
- versionado de checklists;
- gestión documental avanzada;
- motor avanzado de recurrencias;
- CRUD completo de todas las tablas;
- borrado de recursos;
- carga física de archivos;
- paginación avanzada, búsquedas full-text u ordenamiento configurable;
- rate limiting;
- idempotencia basada en `Idempotency-Key`;
- versionado optimista o control de concurrencia explícito en el contrato.

Estas capacidades pueden ser válidas en una evolución futura, pero no forman parte del trabajo necesario para demostrar el MVP actual.

### 2.3 Datos maestros durante el MVP

Para evitar consumir tiempo de desarrollo en pantallas y CRUDs que no son necesarios para demostrar el criterio de éxito, los siguientes datos se consideran precargados mediante seed/migración o administración directa controlada:

- `client`
- `site`
- `installation`
- `equipment`
- `service_type`
- `technician`

La API solo expone las lecturas necesarias para operar sobre ellos.

Esta es una decisión de implementación del MVP, no una limitación futura del dominio.

---

## 3. Convenciones generales

### 3.1 Convenciones HTTP y JSON

| Aspecto | Regla del MVP |
|---|---|
| Base path | `/api/v1` |
| Formato principal | JSON |
| Content-Type requests/responses JSON | `application/json; charset=utf-8` |
| Content-Type de errores | `application/problem+json` |
| Nombres JSON | `camelCase` |
| Identificadores | UUID representado como string |
| Fechas y horas | ISO-8601 con offset, por ejemplo `2026-09-15T09:30:00-03:00` |
| Persistencia temporal | Los timestamps se almacenan como `TIMESTAMPTZ` |
| Valores opcionales | Se representan como `null` cuando forman parte estable de la respuesta |
| Colecciones vacías | Se devuelven como `[]`, no como `null` ni `404` |
| Booleanos | `true` / `false` JSON |
| Duraciones | Minutos enteros positivos cuando el contrato use duración |
| Paginación | No se incorpora en el MVP |
| Autenticación | No forma parte de este contrato MVP |
| Idempotency-Key | No se incorpora en el MVP |
| Rate limiting | No se incorpora en el MVP |

### 3.2 Identificadores UUID

Todos los identificadores primarios y referencias entre recursos se representan como UUID.

Ejemplo:

```json
{
  "id": "8c9fb9c4-93c7-4d13-a347-33262872b95a",
  "equipmentId": "1fc79768-93cd-4f66-a225-08b30153b5de"
}
```

Un UUID con formato inválido produce `400 VALIDATION_ERROR`.

Un UUID válido que no corresponde a un recurso existente produce `404 RESOURCE_NOT_FOUND`.

### 3.3 Fechas y horas

Las fechas/hora enviadas por el cliente deben incluir offset.

Ejemplo válido:

```text
2026-09-14T09:00:00-03:00
```

El backend puede normalizarlas internamente, pero no debe perder el instante temporal representado.

Los intervalos deben respetar las reglas indicadas por cada endpoint, por ejemplo:

```text
startsAt < endsAt
plannedStartAt < plannedEndAt
startedAt <= endedAt
```

### 3.4 Duración estimada

La base de datos define `estimated_duration` como entero pero no fija una unidad. Para eliminar ambigüedad en el contrato HTTP, el MVP adopta **minutos** y expone el campo como:

```json
{
  "estimatedDurationMinutes": 90
}
```

Debe ser un entero mayor que cero.

### 3.5 Campos opcionales, `null` y colecciones vacías

Para evitar interpretaciones distintas entre backend y frontend:

- un recurso relacionado opcional se representa con `null`;
- una colección sin elementos se representa con `[]`;
- no se utiliza `""` para representar ausencia de un valor;
- no se utiliza `404` para indicar una colección vacía;
- los campos definidos como parte estable de una respuesta deben mantener el mismo tipo entre respuestas.

Ejemplos:

```json
{
  "installation": null,
  "assignment": null,
  "interventions": []
}
```

### 3.6 Query parameters

Los query parameters definidos como UUID siguen las mismas reglas de validación que los path parameters.

Los timestamps enviados por query string deben usar ISO-8601 con offset.

No se aceptan filtros adicionales no documentados en cada endpoint.

---

## 3.7 Formato uniforme de errores

Todas las respuestas de error HTTP deben utilizar una estructura compatible con **RFC 9457 (Problem Details for HTTP APIs)**.

El contrato agrega dos extensiones útiles para FieldFlow:

- `code`: código estable y legible por máquina para que el cliente pueda reaccionar sin depender de `detail`;
- `errors`: lista opcional de errores por campo para validaciones de entrada.

Formato base:

```json
{
  "type": "urn:fieldflow:error:validation",
  "title": "Datos de entrada inválidos",
  "status": 400,
  "detail": "Uno o más campos no cumplen el contrato.",
  "instance": "/api/v1/work-orders",
  "code": "VALIDATION_ERROR",
  "traceId": "01J6X8Y2F1A8V9Q7R4M3N2K1P0",
  "errors": [
    {
      "field": "estimatedDurationMinutes",
      "message": "debe ser mayor que cero"
    }
  ]
}
```

`traceId` es opcional durante el MVP. Si la infraestructura ya dispone de un identificador de correlación, debe exponerse aquí; no es requisito crear un sistema de tracing únicamente para cumplir este contrato.

### 3.8 Significado de los campos de error

| Campo | Obligatorio | Uso |
|---|---:|---|
| `type` | Sí | URI/URN estable que clasifica el tipo general de problema |
| `title` | Sí | Descripción breve y estable del problema |
| `status` | Sí | Código HTTP de la respuesta |
| `detail` | Sí | Explicación legible del caso concreto |
| `instance` | Sí | Ruta de la request que produjo el error |
| `code` | Sí | Código funcional estable de FieldFlow |
| `traceId` | No | Correlación con logs/observabilidad |
| `errors` | No | Errores de validación asociados a campos concretos |

El frontend debe tomar decisiones utilizando `status` y/o `code`, **no comparando el texto de `detail`**.

### 3.9 Ejemplos de error

#### Validación — `400`

```json
{
  "type": "urn:fieldflow:error:validation",
  "title": "Datos de entrada inválidos",
  "status": 400,
  "detail": "Uno o más campos no cumplen el contrato.",
  "instance": "/api/v1/work-orders",
  "code": "VALIDATION_ERROR",
  "errors": [
    {
      "field": "estimatedDurationMinutes",
      "message": "debe ser mayor que cero"
    }
  ]
}
```

#### Recurso inexistente — `404`

```json
{
  "type": "urn:fieldflow:error:not-found",
  "title": "Recurso inexistente",
  "status": 404,
  "detail": "No existe la Orden de Trabajo solicitada.",
  "instance": "/api/v1/work-orders/8c9fb9c4-93c7-4d13-a347-33262872b95a",
  "code": "RESOURCE_NOT_FOUND"
}
```

#### Conflicto de agenda — `409`

```json
{
  "type": "urn:fieldflow:error:schedule-overlap",
  "title": "Conflicto de agenda",
  "status": 409,
  "detail": "El técnico ya posee una asignación que se solapa con el intervalo solicitado.",
  "instance": "/api/v1/work-orders/8c9fb9c4-93c7-4d13-a347-33262872b95a/assignment",
  "code": "SCHEDULE_OVERLAP"
}
```

#### Transición inválida — `422`

```json
{
  "type": "urn:fieldflow:error:invalid-status-transition",
  "title": "Transición de estado inválida",
  "status": 422,
  "detail": "El estado solicitado no puede establecerse mediante este endpoint.",
  "instance": "/api/v1/work-orders/8c9fb9c4-93c7-4d13-a347-33262872b95a/status",
  "code": "INVALID_STATUS_TRANSITION"
}
```

### 3.10 Códigos HTTP utilizados

| Código | Uso en FieldFlow |
|---:|---|
| `200 OK` | Consulta o actualización correcta |
| `201 Created` | Recurso creado correctamente |
| `400 Bad Request` | JSON inválido, formato incorrecto, UUID inválido o validación básica de campos |
| `404 Not Found` | El recurso referenciado no existe |
| `409 Conflict` | Conflicto con el estado actual de los datos, unicidad o agenda |
| `422 Unprocessable Content` | La request es válida estructuralmente pero viola una regla funcional o transición permitida |
| `500 Internal Server Error` | Error inesperado del servidor |

No se incorporan `202`, `204`, `401`, `403` ni `429` porque los endpoints del MVP actual no requieren procesamiento asíncrono, respuestas sin cuerpo, autenticación/autorización ni rate limiting.

### 3.11 Catálogo mínimo de códigos de error

Los siguientes códigos cubren las reglas explícitas del contrato. No es necesario crear un código distinto para cada validación trivial.

| HTTP | `code` | Situación |
|---:|---|---|
| `400` | `VALIDATION_ERROR` | Request mal formada, UUID inválido, campo requerido ausente, valor o intervalo básico inválido |
| `404` | `RESOURCE_NOT_FOUND` | El recurso solicitado o referenciado no existe |
| `409` | `TECHNICIAN_NOT_AVAILABLE` | El intervalo solicitado no está contenido en la disponibilidad del técnico |
| `409` | `SCHEDULE_OVERLAP` | La asignación se solapa con otra asignación del mismo técnico |
| `409` | `CHECKLIST_ALREADY_EXISTS` | La OT ya posee su checklist único |
| `409` | `CONFORMITY_ALREADY_EXISTS` | La intervención ya posee conformidad |
| `409` | `DUPLICATE_CHECKLIST_RESPONSE` | Se intenta registrar más de una respuesta para el mismo ítem en una intervención |
| `422` | `INVALID_STATUS_TRANSITION` | Se intenta realizar una transición de OT no habilitada por el endpoint |
| `422` | `TECHNICIAN_NOT_ASSIGNED` | Se intenta iniciar una intervención con un técnico distinto al asignado en el flujo normal del MVP |
| `422` | `CHECKLIST_ITEM_NOT_IN_WORK_ORDER` | Un `checklistItemId` no pertenece al checklist de la OT de la intervención |
| `500` | `INTERNAL_ERROR` | Error inesperado no atribuible a la request |

Para errores `500`, la respuesta no debe exponer stack traces, SQL, credenciales ni detalles internos de infraestructura.

---

## 4. Reglas de ciclo de vida de la Orden de Trabajo

Estados soportados por el esquema:

- `PENDING`
- `ASSIGNED`
- `EN_ROUTE`
- `IN_PROGRESS`
- `PENDING_CUSTOMER_CONFIRMATION`
- `COMPLETED`
- `RESCHEDULED`

Para reducir estados inválidos desde el cliente, varias transiciones son responsabilidad del backend:

| Acción | Estado resultante |
|---|---|
| Crear OT | `PENDING` |
| Crear/actualizar asignación | `ASSIGNED` |
| Técnico informa que está en camino | `EN_ROUTE` |
| Iniciar intervención | `IN_PROGRESS` |
| Enviar reporte de intervención | `PENDING_CUSTOMER_CONFIRMATION` |
| Registrar conformidad | `COMPLETED` |
| Reprogramar | `RESCHEDULED`, hasta confirmar la nueva asignación |

El endpoint genérico de estado queda restringido a `EN_ROUTE` y `RESCHEDULED`. Los demás estados se alcanzan mediante las acciones de negocio correspondientes.

---

# 5. Endpoints del MVP

Todos los paths de esta sección son relativos a `/api/v1`.

Salvo que un endpoint indique lo contrario, pueden aplicarse los errores comunes `400 VALIDATION_ERROR`, `404 RESOURCE_NOT_FOUND` y `500 INTERNAL_ERROR` cuando corresponda. Los errores de negocio específicos se documentan en cada operación o en el catálogo global de errores.

## 5.1 Catálogos operativos

### GET `/equipment`

Devuelve los equipos disponibles para seleccionar al crear una OT, incluyendo contexto de cliente y ubicación.

Query params opcionales:

- `siteId: UUID`
- `clientId: UUID`

Respuesta `200`:

```json
[
  {
    "id": "1fc79768-93cd-4f66-a225-08b30153b5de",
    "identifier": "EQ-001",
    "name": "Compresor principal",
    "currentStatus": "OPERATIONAL",
    "client": {
      "id": "1ec4b8a4-e5dd-4084-a1a9-cd93389bb875",
      "name": "Cliente ACME"
    },
    "site": {
      "id": "aee444d2-a1cf-4ddc-b02c-4bf2f29cc9f4",
      "name": "Planta Córdoba",
      "address": "Dirección de ejemplo"
    },
    "installation": {
      "id": "32521a59-2598-436b-b6b7-36a3d4bb65ee",
      "name": "Sala de máquinas"
    }
  }
]
```

`installation` puede ser `null`.

---

### GET `/service-types`

Devuelve los tipos de servicio existentes.

Respuesta `200`:

```json
[
  {
    "id": "4cc315c5-0b93-4e19-a02d-3902ab688d02",
    "name": "Mantenimiento preventivo"
  }
]
```

---

### GET `/technicians`

Devuelve los técnicos existentes para planificación y asignación.

Respuesta `200`:

```json
[
  {
    "id": "1e1d6a75-b50d-4daa-8a76-c4409302a575",
    "name": "Técnico Uno"
  }
]
```

---

## 5.2 Disponibilidad de técnicos

### POST `/technicians/{technicianId}/availability`

Registra un intervalo de disponibilidad.

Request:

```json
{
  "startsAt": "2026-09-14T08:00:00-03:00",
  "endsAt": "2026-09-14T17:00:00-03:00"
}
```

Reglas:

- `startsAt < endsAt`.
- El técnico debe existir.

Respuesta `201`:

```json
{
  "id": "8cfe0116-83b1-4a19-88c8-9510aa929c18",
  "technicianId": "1e1d6a75-b50d-4daa-8a76-c4409302a575",
  "startsAt": "2026-09-14T08:00:00-03:00",
  "endsAt": "2026-09-14T17:00:00-03:00"
}
```

---

### GET `/technicians/{technicianId}/availability`

Consulta disponibilidad en un rango.

Query params requeridos:

- `from`
- `to`

Respuesta `200`:

```json
[
  {
    "id": "8cfe0116-83b1-4a19-88c8-9510aa929c18",
    "startsAt": "2026-09-14T08:00:00-03:00",
    "endsAt": "2026-09-14T17:00:00-03:00"
  }
]
```

---

## 5.3 Órdenes de Trabajo

### POST `/work-orders`

Crea una Orden de Trabajo.

Request:

```json
{
  "equipmentId": "1fc79768-93cd-4f66-a225-08b30153b5de",
  "serviceTypeId": "4cc315c5-0b93-4e19-a02d-3902ab688d02",
  "instructions": "Revisar vibración y temperatura del equipo.",
  "priority": "HIGH",
  "estimatedDurationMinutes": 90
}
```

Reglas:

- `equipmentId` debe existir.
- `serviceTypeId` debe existir.
- `estimatedDurationMinutes > 0`.
- El estado se establece en `PENDING` desde el backend.

Respuesta `201`:

```json
{
  "id": "8c9fb9c4-93c7-4d13-a347-33262872b95a",
  "equipmentId": "1fc79768-93cd-4f66-a225-08b30153b5de",
  "serviceTypeId": "4cc315c5-0b93-4e19-a02d-3902ab688d02",
  "instructions": "Revisar vibración y temperatura del equipo.",
  "priority": "HIGH",
  "estimatedDurationMinutes": 90,
  "status": "PENDING"
}
```

---

### GET `/work-orders`

Lista Órdenes de Trabajo para seguimiento operativo.

Query params opcionales:

- `status`
- `equipmentId`

Respuesta `200`:

```json
[
  {
    "id": "8c9fb9c4-93c7-4d13-a347-33262872b95a",
    "equipment": {
      "id": "1fc79768-93cd-4f66-a225-08b30153b5de",
      "identifier": "EQ-001",
      "name": "Compresor principal"
    },
    "serviceType": {
      "id": "4cc315c5-0b93-4e19-a02d-3902ab688d02",
      "name": "Mantenimiento preventivo"
    },
    "priority": "HIGH",
    "estimatedDurationMinutes": 90,
    "status": "ASSIGNED"
  }
]
```

No se incorpora paginación en el MVP.

---

### GET `/work-orders/{workOrderId}`

**Endpoint principal de trazabilidad del MVP.**

Debe permitir reconstruir desde una OT toda la información necesaria para responder al criterio de éxito del proyecto.

Respuesta `200`:

```json
{
  "id": "8c9fb9c4-93c7-4d13-a347-33262872b95a",
  "instructions": "Revisar vibración y temperatura del equipo.",
  "priority": "HIGH",
  "estimatedDurationMinutes": 90,
  "status": "PENDING_CUSTOMER_CONFIRMATION",
  "serviceType": {
    "id": "4cc315c5-0b93-4e19-a02d-3902ab688d02",
    "name": "Mantenimiento preventivo"
  },
  "equipment": {
    "id": "1fc79768-93cd-4f66-a225-08b30153b5de",
    "identifier": "EQ-001",
    "name": "Compresor principal",
    "currentStatus": "OPERATIONAL",
    "client": {
      "id": "1ec4b8a4-e5dd-4084-a1a9-cd93389bb875",
      "name": "Cliente ACME"
    },
    "site": {
      "id": "aee444d2-a1cf-4ddc-b02c-4bf2f29cc9f4",
      "name": "Planta Córdoba",
      "address": "Dirección de ejemplo"
    },
    "installation": null
  },
  "assignment": {
    "id": "fb9f85b0-5abe-4d45-91a1-9cdaf97bc91f",
    "technician": {
      "id": "1e1d6a75-b50d-4daa-8a76-c4409302a575",
      "name": "Técnico Uno"
    },
    "plannedStartAt": "2026-09-14T09:00:00-03:00",
    "plannedEndAt": "2026-09-14T10:30:00-03:00"
  },
  "checklist": {
    "id": "93a67117-96cc-4e30-8db2-4bcd15155741",
    "name": "Checklist preventivo",
    "items": [
      {
        "id": "00c9c86e-eb8a-4e16-9694-16cb40866dfc",
        "label": "Verificar temperatura"
      }
    ]
  },
  "interventions": [
    {
      "id": "0b915a57-61f5-4c38-b42d-461162e70698",
      "technician": {
        "id": "1e1d6a75-b50d-4daa-8a76-c4409302a575",
        "name": "Técnico Uno"
      },
      "startedAt": "2026-09-14T09:05:00-03:00",
      "endedAt": "2026-09-14T10:10:00-03:00",
      "status": "PENDING_CUSTOMER_CONFIRMATION",
      "result": "Equipo operativo.",
      "observations": "Se recomienda nueva revisión en 30 días.",
      "failures": [],
      "repairs": [],
      "components": [],
      "checklistResponses": [],
      "technicalNotes": [],
      "evidence": [],
      "conformity": null
    }
  ]
}
```

Notas:

- `assignment` puede ser `null`.
- `checklist` puede ser `null`.
- `installation` puede ser `null`.
- `interventions` puede estar vacío.
- Una OT puede tener múltiples intervenciones.

---

## 5.4 Asignación y agenda

### PUT `/work-orders/{workOrderId}/assignment`

Crea o actualiza la única asignación activa de la OT.

Request:

```json
{
  "technicianId": "1e1d6a75-b50d-4daa-8a76-c4409302a575",
  "plannedStartAt": "2026-09-14T09:00:00-03:00",
  "plannedEndAt": "2026-09-14T10:30:00-03:00"
}
```

Reglas:

1. La OT debe existir.
2. El técnico debe existir.
3. `plannedStartAt < plannedEndAt`.
4. El intervalo debe estar contenido en la disponibilidad del técnico.
5. No debe solaparse con otra asignación del mismo técnico.
6. Al confirmar la asignación, la OT queda en `ASSIGNED`.

Respuesta `200`:

```json
{
  "id": "fb9f85b0-5abe-4d45-91a1-9cdaf97bc91f",
  "workOrderId": "8c9fb9c4-93c7-4d13-a347-33262872b95a",
  "technicianId": "1e1d6a75-b50d-4daa-8a76-c4409302a575",
  "plannedStartAt": "2026-09-14T09:00:00-03:00",
  "plannedEndAt": "2026-09-14T10:30:00-03:00"
}
```

Conflictos relevantes:

- `409 TECHNICIAN_NOT_AVAILABLE`
- `409 SCHEDULE_OVERLAP`

---

### GET `/technicians/{technicianId}/agenda`

La agenda no es una entidad persistente. Este endpoint la obtiene desde las asignaciones.

Query params requeridos:

- `from`
- `to`

Respuesta `200`:

```json
[
  {
    "assignmentId": "fb9f85b0-5abe-4d45-91a1-9cdaf97bc91f",
    "plannedStartAt": "2026-09-14T09:00:00-03:00",
    "plannedEndAt": "2026-09-14T10:30:00-03:00",
    "workOrder": {
      "id": "8c9fb9c4-93c7-4d13-a347-33262872b95a",
      "status": "ASSIGNED",
      "priority": "HIGH",
      "equipmentIdentifier": "EQ-001",
      "equipmentName": "Compresor principal",
      "siteName": "Planta Córdoba",
      "siteAddress": "Dirección de ejemplo"
    }
  }
]
```

---

### PATCH `/work-orders/{workOrderId}/status`

Se utiliza únicamente para estados que representan una acción operativa explícita y no son consecuencia de otro endpoint.

Request permitido:

```json
{
  "status": "EN_ROUTE"
}
```

o:

```json
{
  "status": "RESCHEDULED"
}
```

Cualquier otro destino se rechaza con `422 INVALID_STATUS_TRANSITION` y debe alcanzarse mediante la operación de negocio correspondiente.

---

## 5.5 Checklist de la Orden de Trabajo

### POST `/work-orders/{workOrderId}/checklist`

Crea el único checklist de la OT junto con todos sus ítems en una sola operación.

Request:

```json
{
  "name": "Checklist preventivo",
  "items": [
    {
      "label": "Verificar temperatura"
    },
    {
      "label": "Verificar vibraciones"
    }
  ]
}
```

Respuesta `201`:

```json
{
  "id": "93a67117-96cc-4e30-8db2-4bcd15155741",
  "workOrderId": "8c9fb9c4-93c7-4d13-a347-33262872b95a",
  "name": "Checklist preventivo",
  "items": [
    {
      "id": "00c9c86e-eb8a-4e16-9694-16cb40866dfc",
      "label": "Verificar temperatura"
    }
  ]
}
```

Reglas:

- Una OT solo puede tener un checklist.
- No existe versionado en el MVP.
- Si ya existe, responder `409 CHECKLIST_ALREADY_EXISTS`.

---

## 5.6 Ejecución en campo

### POST `/work-orders/{workOrderId}/interventions`

Inicia una nueva intervención para una OT.

Request:

```json
{
  "technicianId": "1e1d6a75-b50d-4daa-8a76-c4409302a575",
  "startedAt": "2026-09-14T09:05:00-03:00"
}
```

Reglas:

- La OT debe existir.
- El técnico debe existir.
- Para el flujo normal del MVP, el técnico debe coincidir con el técnico asignado a la OT.
- El backend establece el estado de la intervención y de la OT en `IN_PROGRESS`.

Respuesta `201`:

```json
{
  "id": "0b915a57-61f5-4c38-b42d-461162e70698",
  "workOrderId": "8c9fb9c4-93c7-4d13-a347-33262872b95a",
  "technicianId": "1e1d6a75-b50d-4daa-8a76-c4409302a575",
  "startedAt": "2026-09-14T09:05:00-03:00",
  "status": "IN_PROGRESS"
}
```

---

### PUT `/interventions/{interventionId}/report`

Registra el resultado completo de la visita en una sola operación transaccional.

Este endpoint agrupa las entidades de detalle para evitar crear un CRUD independiente para cada tabla.

Request:

```json
{
  "endedAt": "2026-09-14T10:10:00-03:00",
  "result": "Equipo operativo.",
  "observations": "Se recomienda nueva revisión en 30 días.",
  "failures": [
    {
      "description": "Vibración fuera del rango esperado."
    }
  ],
  "repairs": [
    {
      "description": "Ajuste de fijaciones."
    }
  ],
  "components": [
    {
      "componentName": "Soporte del motor",
      "action": "ADJUSTED",
      "description": "Se ajustaron fijaciones."
    }
  ],
  "checklistResponses": [
    {
      "checklistItemId": "00c9c86e-eb8a-4e16-9694-16cb40866dfc",
      "value": "OK",
      "observation": null
    }
  ],
  "technicalNotes": [
    {
      "content": "Controlar vibración en la próxima visita."
    }
  ],
  "evidence": [
    {
      "type": "PHOTO",
      "reference": "evidence/0b915a57/photo-01.jpg",
      "description": "Estado final del equipo."
    }
  ]
}
```

Reglas:

1. `endedAt >= startedAt`.
2. Cada `checklistItemId` debe pertenecer al checklist de la misma OT.
3. No puede haber dos respuestas al mismo ítem dentro de la misma intervención.
4. `evidence.reference` es una referencia lógica; este contrato no define el mecanismo físico de upload/storage.
5. El backend guarda el conjunto de datos de forma transaccional.
6. Al finalizar correctamente, la intervención y la OT pasan a `PENDING_CUSTOMER_CONFIRMATION`.

Respuesta `200`:

```json
{
  "interventionId": "0b915a57-61f5-4c38-b42d-461162e70698",
  "status": "PENDING_CUSTOMER_CONFIRMATION",
  "workOrderStatus": "PENDING_CUSTOMER_CONFIRMATION"
}
```

---

## 5.7 Conformidad

### POST `/interventions/{interventionId}/conformity`

Registra la firma o conformidad del cliente.

Request:

```json
{
  "signature": "opaque-signature-value-or-reference"
}
```

Reglas:

- Una intervención puede tener como máximo una conformidad.
- La intervención debe haber enviado su reporte.
- Tras registrar la conformidad, la OT puede pasar a `COMPLETED`.
- El backend actualiza intervención y OT de manera consistente.

Respuesta `201`:

```json
{
  "id": "71b97a1e-55e8-4e24-b450-a4fb0104de52",
  "interventionId": "0b915a57-61f5-4c38-b42d-461162e70698",
  "signature": "opaque-signature-value-or-reference",
  "workOrderStatus": "COMPLETED"
}
```

Conflicto:

- `409 CONFORMITY_ALREADY_EXISTS`

---

## 5.8 Historial de mantenimiento

### GET `/equipment/{equipmentId}/maintenance-history`

El historial no corresponde a una tabla independiente. Se construye consultando las Órdenes de Trabajo e intervenciones del equipo.

Respuesta `200`:

```json
{
  "equipment": {
    "id": "1fc79768-93cd-4f66-a225-08b30153b5de",
    "identifier": "EQ-001",
    "name": "Compresor principal",
    "currentStatus": "OPERATIONAL"
  },
  "history": [
    {
      "workOrderId": "8c9fb9c4-93c7-4d13-a347-33262872b95a",
      "serviceType": "Mantenimiento preventivo",
      "workOrderStatus": "COMPLETED",
      "interventions": [
        {
          "interventionId": "0b915a57-61f5-4c38-b42d-461162e70698",
          "startedAt": "2026-09-14T09:05:00-03:00",
          "endedAt": "2026-09-14T10:10:00-03:00",
          "technician": "Técnico Uno",
          "result": "Equipo operativo.",
          "observations": "Se recomienda nueva revisión en 30 días.",
          "failures": [],
          "repairs": [],
          "components": [],
          "technicalNotes": [],
          "evidence": [],
          "conformity": {
            "id": "71b97a1e-55e8-4e24-b450-a4fb0104de52"
          }
        }
      ]
    }
  ]
}
```

No se crea ni actualiza una entidad `maintenance_history`.

---

## 5.9 Mantenimiento preventivo

### POST `/equipment/{equipmentId}/preventive-maintenance-plans`

Crea un plan preventivo y su recurrencia simple en una única operación.

Request:

```json
{
  "serviceTypeId": "4cc315c5-0b93-4e19-a02d-3902ab688d02",
  "nextExecutionAt": "2026-10-15T09:00:00-03:00",
  "recurrence": {
    "frequency": "MONTH",
    "interval": 1
  }
}
```

Reglas:

- Equipo y tipo de servicio deben existir.
- `recurrence.interval > 0`.
- El contrato no incorpora expresiones cron ni calendarios avanzados.

Respuesta `201`:

```json
{
  "id": "0dd493de-e301-4e43-b04e-f7006eb0ad0d",
  "equipmentId": "1fc79768-93cd-4f66-a225-08b30153b5de",
  "serviceTypeId": "4cc315c5-0b93-4e19-a02d-3902ab688d02",
  "nextExecutionAt": "2026-10-15T09:00:00-03:00",
  "recurrence": {
    "id": "4868476d-f452-40b7-a9df-154027776ffe",
    "frequency": "MONTH",
    "interval": 1
  }
}
```

---

### GET `/equipment/{equipmentId}/preventive-maintenance-plans`

Lista los planes preventivos del equipo.

Respuesta `200`:

```json
[
  {
    "id": "0dd493de-e301-4e43-b04e-f7006eb0ad0d",
    "serviceType": {
      "id": "4cc315c5-0b93-4e19-a02d-3902ab688d02",
      "name": "Mantenimiento preventivo"
    },
    "nextExecutionAt": "2026-10-15T09:00:00-03:00",
    "recurrence": {
      "frequency": "MONTH",
      "interval": 1
    }
  }
]
```

El MVP no incluye un scheduler avanzado ni una API independiente para reglas de recurrencia.

---

# 6. Resumen del contrato

| # | Método | Ruta | Objetivo |
|---:|---|---|---|
| 1 | GET | `/equipment` | Seleccionar equipo con cliente/ubicación |
| 2 | GET | `/service-types` | Consultar tipos de servicio |
| 3 | GET | `/technicians` | Consultar técnicos |
| 4 | POST | `/technicians/{id}/availability` | Registrar disponibilidad |
| 5 | GET | `/technicians/{id}/availability` | Consultar disponibilidad |
| 6 | POST | `/work-orders` | Crear OT |
| 7 | GET | `/work-orders` | Seguimiento de OTs |
| 8 | GET | `/work-orders/{id}` | Vista completa y trazabilidad de una OT |
| 9 | PUT | `/work-orders/{id}/assignment` | Asignar/reprogramar técnico |
| 10 | GET | `/technicians/{id}/agenda` | Obtener agenda desde asignaciones |
| 11 | PATCH | `/work-orders/{id}/status` | `EN_ROUTE` / `RESCHEDULED` |
| 12 | POST | `/work-orders/{id}/checklist` | Crear checklist e ítems |
| 13 | POST | `/work-orders/{id}/interventions` | Iniciar intervención |
| 14 | PUT | `/interventions/{id}/report` | Registrar ejecución completa |
| 15 | POST | `/interventions/{id}/conformity` | Registrar conformidad |
| 16 | GET | `/equipment/{id}/maintenance-history` | Consultar historial derivado |
| 17 | POST | `/equipment/{id}/preventive-maintenance-plans` | Crear mantenimiento preventivo |
| 18 | GET | `/equipment/{id}/preventive-maintenance-plans` | Consultar planes preventivos |

**Total del contrato MVP: 18 endpoints.**

---

# 7. Endpoints explícitamente post-MVP

Estos endpoints no deben incorporarse durante las cuatro semanas restantes salvo que el flujo principal esté terminado, probado y congelado:

```text
POST/PATCH/DELETE /clients/**
POST/PATCH/DELETE /sites/**
POST/PATCH/DELETE /installations/**
POST/PATCH/DELETE /equipment/**
POST/PATCH/DELETE /service-types/**
POST/PATCH/DELETE /technicians/**
DELETE /work-orders/**
DELETE /interventions/**
/api/v1/users/**
/api/v1/roles/**
/api/v1/schedule-events/**
/api/v1/maintenance-history/**
/api/v1/components/**
/api/v1/checklist-versions/**
```

Tampoco se agregan endpoints individuales de CRUD para `failure`, `repair`, `intervention_component`, `checklist_response`, `technical_note` o `evidence`; esas escrituras forman parte del reporte transaccional de intervención.

---

# 8. Criterio de aceptación global del contrato

El contrato se considera suficiente para el MVP cuando permite ejecutar de punta a punta este escenario:

1. Seleccionar un equipo existente y conocer su cliente/ubicación.
2. Crear una OT.
3. Consultar disponibilidad.
4. Asignar un técnico.
5. Consultar su agenda.
6. Marcar salida a campo.
7. Iniciar la intervención.
8. Registrar checklist, fallas/reparaciones/componentes, notas y evidencia.
9. Dejar la OT pendiente de conformidad.
10. Registrar conformidad.
11. Consultar la OT completa en estado `COMPLETED`.
12. Consultar esa intervención desde el historial del equipo.
13. Crear y consultar una recurrencia preventiva simple.

Si una nueva ruta no es necesaria para completar alguno de estos pasos o una capacidad expresamente definida en las fuentes del MVP, se considera fuera de alcance.
