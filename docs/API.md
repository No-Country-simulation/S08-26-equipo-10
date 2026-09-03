# API.md - Contrato de API MVP de FieldFlow

**Este documento es el contrato entre el backend y el frontend.** Define la API REST v1 para administrar, planificar, ejecutar y dar trazabilidad a servicios tecnicos en campo.

- **Version:** v1 MVP
- **Base URL local:** `http://localhost:8080/api/v1`
- **Formato:** JSON, salvo carga y descarga de archivos de evidencia
- **Especificacion viva:** `http://localhost:8080/v3/api-docs`

## Como cambiar este contrato

1. Abrir un PR que modifique este archivo.
2. Revisar el cambio con los equipos backend y frontend.
3. Aprobar los cambios antes de implementar.

Quitar o renombrar campos, cambiar tipos, cambiar obligatoriedad o cambiar codigos de estado son cambios incompatibles. Agregar campos opcionales o endpoints nuevos son cambios compatibles.

## Convenciones generales

| Aspecto | Regla |
|---|---|
| Formato | JSON con `Content-Type: application/json; charset=utf-8` |
| Nombres | `camelCase` en JSON; corresponden a los nombres `snake_case` del modelo |
| Fechas | ISO-8601 con zona y siempre UTC; las fechas sin hora usan `YYYY-MM-DD` |
| Identificadores | UUID como string |
| Nulos | `null` explicito; no se reemplaza por `""` |
| Paginacion | `?page=0&size=20&sort=createdAt,desc` |
| Limite de pagina | `size` entre 1 y 100; valor predeterminado 20 |
| Idempotencia | Header `Idempotency-Key` UUID en los POST de creacion o carga |
| Archivos | `multipart/form-data`; el JSON asociado viaja en el campo `metadata` |
| Autenticacion | `Authorization: Bearer <token>` |

Las fechas de negocio se almacenan como `timestamptz`. Las respuestas pueden incluir proyecciones de entidades relacionadas, pero sus campos deben corresponder al modelo definido.

### Roles

Los unicos codigos persistidos en `role.code` son:

- `ADMINISTRATION`: administra clientes y activos, recibe solicitudes, crea OT, planifica, asigna y supervisa.
- `TECHNICIAN`: consulta trabajos asignados, ejecuta intervenciones y registra checklist, notas, evidencia y conformidad.

Un usuario puede tener uno o ambos roles mediante `userRole`. `ADMIN` y `VIEWER` no son roles persistidos en este contrato.

## Formato de error (RFC 9457)

Todos los errores usan `application/problem+json`.

```json
{
  "type": "https://fieldflow.example/errors/validation",
  "title": "Datos de entrada invalidos",
  "status": 400,
  "detail": "Uno o mas campos no cumplen las reglas requeridas",
  "instance": "/api/v1/work-orders",
  "traceId": "0af7651916cd43dd8448eb211c80319c",
  "errors": [
    { "field": "priority", "message": "Valor no permitido" }
  ]
}
```

`errors` es un arreglo y puede estar vacio. Los tipos de error son `validation`, `authentication`, `authorization`, `not-found`, `conflict`, `business-rule`, `rate-limit` e `internal`.

## Codigos de estado

`200` OK · `201` Creado · `204` Sin contenido · `400` Entrada invalida · `401` Sin autenticar · `403` Sin permiso · `404` No existe · `409` Conflicto · `422` Regla de negocio violada · `413` Archivo demasiado grande · `415` Tipo no soportado · `429` Demasiadas peticiones · `500` Error del servidor · `503` Servicio no disponible.

---

## 1. Autenticacion y usuarios

### `POST /auth/login`

Inicia sesion con email y contrasena. **Roles:** publico.

**Request**

```json
{
  "email": "tecnico@fieldflow.com",
  "password": "secreto"
}
```

**Response `200`**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresAt": "2026-09-02T22:30:00Z",
  "user": {
    "id": "0192f3a1-0000-4000-8000-000000000001",
    "name": "Luis Gomez",
    "email": "tecnico@fieldflow.com",
    "active": true,
    "roles": ["TECHNICIAN"]
  }
}
```

**Errores:** `400`, `401`, `429`, `500`.

### `POST /auth/logout`

Invalida el token actual. **Autenticacion:** cualquier usuario autenticado.

**Response `204`:** sin cuerpo.

**Errores:** `401`, `500`.

### `GET /auth/me`

Devuelve el usuario autenticado. **Autenticacion:** cualquier usuario autenticado.

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000001",
  "name": "Luis Gomez",
  "email": "tecnico@fieldflow.com",
  "active": true,
  "roles": ["TECHNICIAN"]
}
```

**Errores:** `401`, `500`.

### `POST /users`

Crea un usuario interno y sus roles. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "name": "Luis Gomez",
  "email": "tecnico@fieldflow.com",
  "password": "secreto",
  "roles": ["TECHNICIAN"]
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000001",
  "name": "Luis Gomez",
  "email": "tecnico@fieldflow.com",
  "active": true,
  "roles": ["TECHNICIAN"],
  "createdAt": "2026-09-02T14:30:00Z",
  "updatedAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `400`, `401`, `403`, `409`, `422`, `500`.

---

## 2. Clientes y activos

### `POST /clients`

Crea un cliente. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "legalName": "Acme Industrial",
  "taxIdentifier": "30-71234567-8"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000010",
  "legalName": "Acme Industrial",
  "taxIdentifier": "30-71234567-8",
  "active": true,
  "createdAt": "2026-09-02T14:30:00Z",
  "updatedAt": "2026-09-02T14:30:00Z"
}
```

### `GET /clients`

Lista clientes. **Roles:** `ADMINISTRATION`; un tecnico solo ve clientes relacionados con sus OT asignadas.

**Response `200`**

```json
{
  "content": [
    {
      "id": "0192f3a1-0000-4000-8000-000000000010",
      "legalName": "Acme Industrial",
      "taxIdentifier": "30-71234567-8",
      "active": true
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

### `GET /clients/{clientId}`

Devuelve un cliente. **Roles:** `ADMINISTRATION`; un tecnico requiere alcance sobre una OT asignada.

### `PATCH /clients/{clientId}`

Actualiza `legalName`, `taxIdentifier` o `active`. **Roles:** `ADMINISTRATION`.

**Request**

```json
{
  "legalName": "Acme Industrial SA",
  "active": true
}
```

**Response:** `200` con el recurso actualizado.

### `POST /clients/{clientId}/sites`

Crea una sede perteneciente al cliente. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "name": "Planta Norte",
  "address": "Av. Central 123",
  "operationalContact": "Carlos Ruiz"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000011",
  "clientId": "0192f3a1-0000-4000-8000-000000000010",
  "name": "Planta Norte",
  "address": "Av. Central 123",
  "operationalContact": "Carlos Ruiz",
  "active": true
}
```

### `GET /sites/{siteId}`

Devuelve una sede, su cliente e instalaciones. **Roles:** `ADMINISTRATION`; un tecnico requiere alcance sobre una OT asignada.

### `PATCH /sites/{siteId}`

Actualiza los datos de una sede o su estado `active`. **Roles:** `ADMINISTRATION`.

### `POST /sites/{siteId}/installations`

Crea una instalacion dentro de una sede. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "name": "Sala de compresores",
  "description": "Area tecnica norte"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000012",
  "siteId": "0192f3a1-0000-4000-8000-000000000011",
  "name": "Sala de compresores",
  "description": "Area tecnica norte",
  "active": true
}
```

### `GET /installations/{installationId}`

Devuelve una instalacion y sus equipos. **Roles:** `ADMINISTRATION`; un tecnico requiere alcance sobre una OT asignada.

### `POST /installations/{installationId}/equipment`

Crea un equipo dentro de una instalacion. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "assetIdentifier": "CMP-001",
  "name": "Compresor principal",
  "brand": "IndustrialTech",
  "model": "AC-500",
  "currentStatus": "OPERATIONAL"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000020",
  "installationId": "0192f3a1-0000-4000-8000-000000000012",
  "assetIdentifier": "CMP-001",
  "name": "Compresor principal",
  "brand": "IndustrialTech",
  "model": "AC-500",
  "currentStatus": "OPERATIONAL",
  "active": true,
  "createdAt": "2026-09-02T14:30:00Z",
  "updatedAt": "2026-09-02T14:30:00Z"
}
```

### `GET /equipment/{equipmentId}`

Devuelve un equipo. **Roles:** `ADMINISTRATION`; un tecnico requiere alcance sobre una OT asignada.

### `PATCH /equipment/{equipmentId}`

Actualiza datos del equipo o su estado `active`. **Roles:** `ADMINISTRATION`.

**Errores comunes del modulo:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

---

## 3. Solicitudes, tipos de servicio y ordenes de trabajo

### `POST /service-requests`

Registra una necesidad de mantenimiento. `equipmentId` es opcional cuando el activo aun no esta identificado. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "clientId": "0192f3a1-0000-4000-8000-000000000010",
  "siteId": "0192f3a1-0000-4000-8000-000000000011",
  "equipmentId": "0192f3a1-0000-4000-8000-000000000020",
  "description": "El compresor presenta vibracion inusual",
  "receivedAt": "2026-09-02T14:30:00Z"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000005",
  "clientId": "0192f3a1-0000-4000-8000-000000000010",
  "siteId": "0192f3a1-0000-4000-8000-000000000011",
  "equipmentId": "0192f3a1-0000-4000-8000-000000000020",
  "description": "El compresor presenta vibracion inusual",
  "receivedAt": "2026-09-02T14:30:00Z",
  "status": "RECEIVED",
  "createdAt": "2026-09-02T14:30:00Z"
}
```

### `POST /service-types`

Crea un tipo de servicio. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "name": "Mantenimiento preventivo de compresor",
  "description": "Inspeccion y mantenimiento programado"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000030",
  "name": "Mantenimiento preventivo de compresor",
  "description": "Inspeccion y mantenimiento programado",
  "active": true
}
```

### `GET /service-types`

Lista tipos de servicio. **Roles:** cualquier usuario autenticado.

### `POST /service-requests/{requestId}/work-orders`

Convierte una solicitud en una OT. La solicitud pasa a `CONVERTED`. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "serviceTypeId": "0192f3a1-0000-4000-8000-000000000030",
  "title": "Revision de compresor",
  "instructions": "Coordinar parada con el responsable de planta",
  "priority": "HIGH",
  "estimatedDurationMinutes": 180,
  "dueAt": "2026-09-10T23:59:59Z"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000050",
  "number": "OT-000050",
  "serviceRequestId": "0192f3a1-0000-4000-8000-000000000005",
  "clientId": "0192f3a1-0000-4000-8000-000000000010",
  "siteId": "0192f3a1-0000-4000-8000-000000000011",
  "installationId": "0192f3a1-0000-4000-8000-000000000012",
  "equipmentId": "0192f3a1-0000-4000-8000-000000000020",
  "siteNameSnapshot": "Planta Norte",
  "siteAddressSnapshot": "Av. Central 123",
  "installationNameSnapshot": "Sala de compresores",
  "equipmentIdentifierSnapshot": "CMP-001",
  "serviceTypeId": "0192f3a1-0000-4000-8000-000000000030",
  "title": "Revision de compresor",
  "instructions": "Coordinar parada con el responsable de planta",
  "priority": "HIGH",
  "estimatedDurationMinutes": 180,
  "dueAt": "2026-09-10T23:59:59Z",
  "status": "PENDING",
  "createdAt": "2026-09-02T14:30:00Z",
  "updatedAt": "2026-09-02T14:30:00Z"
}
```

### `POST /work-orders`

Crea directamente una OT. Para mantenimiento preventivo, se debe informar `preventiveMaintenancePlanId` en lugar de `serviceRequestId`. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "serviceRequestId": null,
  "preventiveMaintenancePlanId": "0192f3a1-0000-4000-0000-000000000070",
  "clientId": "0192f3a1-0000-4000-0000-000000000010",
  "siteId": "0192f3a1-0000-4000-0000-000000000011",
  "installationId": "0192f3a1-0000-4000-0000-000000000012",
  "equipmentId": "0192f3a1-0000-4000-0000-000000000020",
  "siteNameSnapshot": "Planta Norte",
  "siteAddressSnapshot": "Av. Central 123",
  "installationNameSnapshot": "Sala de compresores",
  "equipmentIdentifierSnapshot": "CMP-001",
  "serviceTypeId": "0192f3a1-0000-4000-0000-000000000030",
  "title": "Mantenimiento preventivo",
  "instructions": "Realizar inspeccion general",
  "priority": "MEDIUM",
  "estimatedDurationMinutes": 180,
  "dueAt": "2026-11-20T09:00:00Z"
}
```

### `GET /work-orders`

Lista y filtra OT por `status`, `priority`, `clientId`, `siteId`, `equipmentId` y tecnico asignado. **Roles:** cualquier usuario autenticado; el tecnico solo ve sus asignaciones.

### `GET /work-orders/{workOrderId}`

Devuelve la OT, sus asignaciones, intervenciones, evidencia, conformidad e historial de estados. **Roles:** cualquier usuario autenticado con alcance sobre la OT.

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-0000-000000000050",
  "number": "OT-000050",
  "serviceRequestId": null,
  "preventiveMaintenancePlanId": "0192f3a1-0000-4000-0000-000000000070",
  "clientId": "0192f3a1-0000-4000-0000-000000000010",
  "siteId": "0192f3a1-0000-4000-0000-000000000011",
  "installationId": "0192f3a1-0000-4000-0000-000000000012",
  "equipmentId": "0192f3a1-0000-4000-0000-000000000020",
  "siteNameSnapshot": "Planta Norte",
  "siteAddressSnapshot": "Av. Central 123",
  "installationNameSnapshot": "Sala de compresores",
  "equipmentIdentifierSnapshot": "CMP-001",
  "serviceTypeId": "0192f3a1-0000-4000-0000-000000000030",
  "title": "Mantenimiento preventivo",
  "instructions": "Realizar inspeccion general",
  "priority": "MEDIUM",
  "estimatedDurationMinutes": 180,
  "dueAt": "2026-11-20T09:00:00Z",
  "status": "PENDING",
  "assignments": [],
  "interventions": [],
  "statusHistory": [],
  "createdAt": "2026-09-02T14:30:00Z",
  "updatedAt": "2026-09-02T14:30:00Z"
}
```

### `PATCH /work-orders/{workOrderId}`

Actualiza datos editables de la OT. **Roles:** `ADMINISTRATION`.

### `PATCH /work-orders/{workOrderId}/status`

Cambia el estado de la OT respetando las transiciones permitidas. **Roles:** `ADMINISTRATION`; `TECHNICIAN` puede usar `EN_ROUTE` para una OT asignada.

**Request**

```json
{
  "status": "EN_ROUTE",
  "reason": null
}
```

### `GET /work-orders/{workOrderId}/status-history`

Devuelve los registros de `work_order_status_history` en orden cronologico. **Roles:** cualquier usuario con alcance sobre la OT.

**Errores comunes del modulo:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

---

## 4. Planificacion y agenda

### `GET /technicians`

Lista usuarios activos que tienen el rol `TECHNICIAN`. No crea una entidad `technician` separada. **Roles:** `ADMINISTRATION`; un tecnico puede consultar su propio usuario.

### `GET /technicians/{technicianId}/availability`

Consulta intervalos de disponibilidad. **Roles:** `ADMINISTRATION`; un tecnico puede consultar su propia disponibilidad.

### `PUT /technicians/{technicianId}/availability`

Reemplaza intervalos de disponibilidad para un periodo. **Roles:** `ADMINISTRATION`; un tecnico solo puede modificar la propia.

**Request**

```json
{
  "slots": [
    {
      "startsAt": "2026-09-08T09:00:00Z",
      "endsAt": "2026-09-08T17:00:00Z",
      "status": "AVAILABLE",
      "notes": null
    }
  ]
}
```

### `POST /work-orders/{workOrderId}/assignments`

Crea una asignacion activa para un tecnico disponible. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "technicianId": "0192f3a1-0000-4000-0000-000000000040",
  "assignedByUserId": "0192f3a1-0000-4000-0000-000000000001",
  "plannedStartAt": "2026-09-08T09:00:00Z",
  "plannedEndAt": "2026-09-08T12:00:00Z",
  "status": "ACTIVE",
  "reschedulingReason": null
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-0000-000000000051",
  "workOrderId": "0192f3a1-0000-4000-0000-000000000050",
  "technicianId": "0192f3a1-0000-4000-0000-000000000040",
  "assignedByUserId": "0192f3a1-0000-4000-0000-000000000001",
  "plannedStartAt": "2026-09-08T09:00:00Z",
  "plannedEndAt": "2026-09-08T12:00:00Z",
  "status": "ACTIVE",
  "reschedulingReason": null,
  "createdAt": "2026-09-02T14:30:00Z"
}
```

### `POST /assignments/{assignmentId}/schedule-events`

Crea un bloque de agenda para una asignacion. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "startsAt": "2026-09-08T09:00:00Z",
  "endsAt": "2026-09-08T12:00:00Z",
  "status": "SCHEDULED"
}
```

### `GET /calendar`

Consulta `scheduleEvent` en un intervalo `from` y `to`. **Roles:** cualquier usuario autenticado; el tecnico solo ve sus eventos.

**Errores comunes del modulo:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

---

## 5. Ejecucion en campo

### `POST /work-orders/{workOrderId}/interventions`

Registra una visita e intervencion. Debe vincularse a una asignacion. **Roles:** `TECHNICIAN` asignado o `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "assignmentId": "0192f3a1-0000-4000-0000-000000000051",
  "technicianId": "0192f3a1-0000-4000-0000-000000000040",
  "actualStartedAt": "2026-09-08T09:12:00Z",
  "result": null,
  "observations": null
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-0000-000000000060",
  "workOrderId": "0192f3a1-0000-4000-0000-000000000050",
  "assignmentId": "0192f3a1-0000-4000-0000-000000000051",
  "technicianId": "0192f3a1-0000-4000-0000-000000000040",
  "actualStartedAt": "2026-09-08T09:12:00Z",
  "actualEndedAt": null,
  "result": null,
  "observations": null,
  "createdAt": "2026-09-08T09:12:00Z",
  "updatedAt": "2026-09-08T09:12:00Z"
}
```

### `PATCH /interventions/{interventionId}`

Actualiza el resultado, observaciones y finalizacion de una intervencion. **Roles:** tecnico propietario de la asignacion o `ADMINISTRATION`.

**Request**

```json
{
  "actualEndedAt": "2026-09-08T11:45:00Z",
  "result": "COMPLETED",
  "observations": "Equipo operativo luego del ajuste"
}
```

### `POST /interventions/{interventionId}/failures`

Registra una falla. **Roles:** tecnico de la intervencion o `ADMINISTRATION`.

**Request**

```json
{
  "description": "Correa floja",
  "severity": "MEDIUM",
  "detectedAt": "2026-09-08T09:30:00Z",
  "resolved": true
}
```

### `POST /interventions/{interventionId}/repairs`

Registra una reparacion. **Roles:** tecnico de la intervencion o `ADMINISTRATION`.

**Request**

```json
{
  "description": "Ajuste de correa",
  "status": "COMPLETED",
  "completedAt": "2026-09-08T10:30:00Z",
  "notes": null
}
```

### `POST /equipment/{equipmentId}/components`

Registra un componente del equipo. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "name": "Correa principal",
  "partNumber": "BELT-22",
  "serialNumber": null,
  "currentStatus": "OPERATIONAL",
  "active": true
}
```

### `POST /interventions/{interventionId}/components`

Registra un componente intervenido. El componente debe pertenecer al equipo de la OT. **Roles:** tecnico de la intervencion o `ADMINISTRATION`.

**Request**

```json
{
  "equipmentComponentId": "0192f3a1-0000-4000-0000-000000000071",
  "action": "REPAIRED",
  "description": "Ajuste de correa"
}
```

**Errores comunes del modulo:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

---

## 6. Checklists, notas y evidencia

### `GET /checklists?serviceTypeId={serviceTypeId}`

Devuelve la plantilla activa de un tipo de servicio. **Roles:** cualquier usuario autenticado.

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-0000-000000000031",
  "serviceTypeId": "0192f3a1-0000-4000-0000-000000000030",
  "name": "Revision de compresor",
  "version": 2,
  "active": true,
  "items": [
    {
      "id": "0192f3a1-0000-4000-0000-000000000032",
      "checklistId": "0192f3a1-0000-4000-0000-000000000031",
      "position": 1,
      "label": "Verificar nivel de aceite",
      "responseType": "BOOLEAN",
      "required": true
    }
  ]
}
```

### `POST /checklists`

Crea una version de checklist. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "serviceTypeId": "0192f3a1-0000-4000-0000-000000000030",
  "name": "Revision de compresor",
  "version": 3,
  "active": true,
  "items": [
    {
      "position": 1,
      "label": "Verificar nivel de aceite",
      "responseType": "BOOLEAN",
      "required": true
    }
  ]
}
```

### `PUT /interventions/{interventionId}/checklist-responses`

Guarda respuestas de checklist. Permite guardado parcial. **Roles:** tecnico de la intervencion o `ADMINISTRATION`.

**Request**

```json
{
  "responses": [
    {
      "checklistItemId": "0192f3a1-0000-4000-0000-000000000032",
      "value": true,
      "observation": null,
      "answeredAt": "2026-09-08T10:00:00Z"
    }
  ]
}
```

### `POST /interventions/{interventionId}/technical-notes`

Registra una nota tecnica. **Roles:** tecnico de la intervencion o `ADMINISTRATION`.

**Request**

```json
{
  "authorUserId": "0192f3a1-0000-4000-0000-000000000040",
  "content": "Se ajusto correa y se reemplazo filtro"
}
```

### `POST /interventions/{interventionId}/evidence`

Carga evidencia y la vincula a la intervencion. **Roles:** tecnico de la intervencion o `ADMINISTRATION`. Header `Idempotency-Key` requerido.

Campo `file`: binario. Campo `metadata`:

```json
{
  "type": "PHOTO",
  "fileName": "filtro-reemplazado.jpg",
  "storageLocation": "fieldflow/interventions/0192.../filtro-reemplazado.jpg",
  "mimeType": "image/jpeg",
  "sizeBytes": 248912,
  "description": "Filtro reemplazado",
  "capturedAt": "2026-09-08T10:40:00Z"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-0000-000000000061",
  "interventionId": "0192f3a1-0000-4000-0000-000000000060",
  "type": "PHOTO",
  "fileName": "filtro-reemplazado.jpg",
  "storageLocation": "fieldflow/interventions/0192.../filtro-reemplazado.jpg",
  "mimeType": "image/jpeg",
  "sizeBytes": 248912,
  "description": "Filtro reemplazado",
  "capturedAt": "2026-09-08T10:40:00Z",
  "createdAt": "2026-09-08T10:41:00Z"
}
```

### `GET /interventions/{interventionId}/evidence`

Lista la evidencia de una intervencion. **Roles:** cualquier usuario con alcance sobre la OT.

### `GET /evidence/{evidenceId}/download`

Descarga el binario asociado a la evidencia. **Roles:** cualquier usuario con alcance sobre la OT.

---

## 7. Conformidad e historial

### `POST /interventions/{interventionId}/conformity`

Registra la aceptacion o firma del cliente. Solo puede existir una conformidad por intervencion. **Roles:** tecnico de la intervencion o `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "signerName": "Carlos Ruiz",
  "customerRole": "Responsable de planta",
  "signerIdentifier": null,
  "signature": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUg...",
  "acceptedAt": "2026-09-08T12:00:00Z",
  "observations": "Trabajo realizado conforme"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-0000-000000000062",
  "interventionId": "0192f3a1-0000-4000-0000-000000000060",
  "signerName": "Carlos Ruiz",
  "customerRole": "Responsable de planta",
  "signerIdentifier": null,
  "signature": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUg...",
  "acceptedAt": "2026-09-08T12:00:00Z",
  "observations": "Trabajo realizado conforme"
}
```

Una conformidad con `acceptedAt` no nulo permite que la OT pase a `COMPLETED`. Una conformidad sin aceptacion debe conservar la OT en `PENDING_CUSTOMER_CONFIRMATION`.

### `GET /equipment/{equipmentId}/maintenance-history`

Consulta el historial derivado de intervenciones completadas, OT, fallas, reparaciones, componentes, notas, evidencia, checklist y conformidad. No crea una tabla fuente `maintenanceHistory`. **Roles:** `ADMINISTRATION`; un tecnico requiere alcance sobre una OT asignada.

**Response `200`**

```json
{
  "equipmentId": "0192f3a1-0000-4000-0000-000000000020",
  "items": [
    {
      "workOrderId": "0192f3a1-0000-4000-0000-000000000050",
      "interventionId": "0192f3a1-0000-4000-0000-000000000060",
      "status": "COMPLETED",
      "actualEndedAt": "2026-09-08T11:45:00Z",
      "technicianId": "0192f3a1-0000-4000-0000-000000000040",
      "failures": [
        {
          "id": "0192f3a1-0000-4000-0000-000000000080",
          "description": "Correa floja",
          "severity": "MEDIUM",
          "detectedAt": "2026-09-08T09:30:00Z",
          "resolved": true
        }
      ],
      "repairs": [
        {
          "id": "0192f3a1-0000-4000-0000-000000000081",
          "description": "Ajuste de correa",
          "status": "COMPLETED",
          "completedAt": "2026-09-08T10:30:00Z",
          "notes": null
        }
      ],
      "componentsIntervened": [
        {
          "id": "0192f3a1-0000-4000-0000-000000000082",
          "equipmentComponentId": "0192f3a1-0000-4000-0000-000000000071",
          "action": "REPAIRED",
          "description": "Ajuste de correa",
          "createdAt": "2026-09-08T10:30:00Z"
        }
      ],
      "technicalNotes": [
        {
          "id": "0192f3a1-0000-4000-0000-000000000083",
          "authorUserId": "0192f3a1-0000-4000-0000-000000000040",
          "content": "Se ajusto correa y se reemplazo filtro",
          "createdAt": "2026-09-08T11:45:00Z"
        }
      ],
      "evidence": [
        {
          "id": "0192f3a1-0000-4000-0000-000000000084",
          "type": "PHOTO",
          "fileName": "filtro-reemplazado.jpg",
          "storageLocation": "fieldflow/interventions/0192.../filtro-reemplazado.jpg",
          "mimeType": "image/jpeg",
          "sizeBytes": 248912,
          "description": "Filtro reemplazado",
          "capturedAt": "2026-09-08T10:40:00Z",
          "createdAt": "2026-09-08T10:41:00Z"
        }
      ],
      "checklistResponses": [
        {
          "id": "0192f3a1-0000-4000-0000-000000000085",
          "checklistItemId": "0192f3a1-0000-4000-0000-000000000032",
          "value": true,
          "observation": null,
          "answeredAt": "2026-09-08T10:00:00Z"
        }
      ],
      "conformity": {
        "id": "0192f3a1-0000-4000-0000-000000000086",
        "signerName": "Carlos Ruiz",
        "customerRole": "Responsable de planta",
        "signerIdentifier": null,
        "signature": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUg...",
        "acceptedAt": "2026-09-08T12:00:00Z",
        "observations": "Trabajo realizado conforme"
      }
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 8. Mantenimiento preventivo

### `POST /preventive-maintenance-plans`

Crea un plan preventivo y su recurrencia. **Roles:** `ADMINISTRATION`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "equipmentId": "0192f3a1-0000-4000-0000-000000000020",
  "serviceTypeId": "0192f3a1-0000-4000-0000-000000000030",
  "name": "Mantenimiento preventivo del compresor",
  "description": "Inspeccion y mantenimiento programado",
  "startsOn": "2026-08-20",
  "nextExecutionAt": "2026-11-20T09:00:00Z",
  "active": true,
  "recurrence": {
    "frequency": "MONTHLY",
    "interval": 3,
    "dayOfMonth": 20,
    "dayOfWeek": null,
    "endsOn": null
  }
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-0000-000000000070",
  "equipmentId": "0192f3a1-0000-4000-0000-000000000020",
  "serviceTypeId": "0192f3a1-0000-4000-0000-000000000030",
  "name": "Mantenimiento preventivo del compresor",
  "description": "Inspeccion y mantenimiento programado",
  "startsOn": "2026-08-20",
  "nextExecutionAt": "2026-11-20T09:00:00Z",
  "active": true,
  "recurrence": {
    "id": "0192f3a1-0000-4000-0000-000000000071",
    "frequency": "MONTHLY",
    "interval": 3,
    "dayOfMonth": 20,
    "dayOfWeek": null,
    "endsOn": null
  },
  "createdAt": "2026-09-02T14:30:00Z",
  "updatedAt": "2026-09-02T14:30:00Z"
}
```

### `GET /preventive-maintenance-plans`

Lista planes preventivos. **Roles:** `ADMINISTRATION`; un tecnico solo ve planes relacionados con sus OT asignadas.

### `GET /preventive-maintenance-plans/{planId}`

Devuelve un plan y su recurrencia. **Roles:** `ADMINISTRATION`.

### `PATCH /preventive-maintenance-plans/{planId}`

Actualiza `name`, `description`, `nextExecutionAt`, `active` o la recurrencia. **Roles:** `ADMINISTRATION`.

**Request**

```json
{
  "nextExecutionAt": "2026-12-01T09:00:00Z",
  "active": false
}
```

Cada ejecucion preventiva genera una OT con `preventiveMaintenancePlanId` y sigue el flujo normal de asignacion, agenda, intervencion, evidencia, conformidad e historial.

---

## 9. Reglas de negocio MVP

- Los roles persistidos son solo `ADMINISTRATION` y `TECHNICIAN`; la relacion de usuario y rol es muchos a muchos.
- Un `serviceRequest` puede originar cero o una OT. Su estado es `RECEIVED`, `CONVERTED` o `CANCELLED`.
- Una OT debe pertenecer a un cliente, una sede y un tipo de servicio; instalacion y equipo pueden ser nulos.
- Una OT de solicitud usa `serviceRequestId`; una OT preventiva usa `preventiveMaintenancePlanId`.
- Los estados de OT son `PENDING`, `ASSIGNED`, `EN_ROUTE`, `IN_PROGRESS`, `PENDING_CUSTOMER_CONFIRMATION`, `COMPLETED` y `RESCHEDULED`. `CANCELLED` no forma parte de v1.
- Como maximo existe una asignacion `ACTIVE` por OT. Las asignaciones anteriores conservan `REPLACED`, `CANCELLED` o `COMPLETED`.
- Una asignacion valida requiere un usuario con rol `TECHNICIAN` y una franja compatible con su disponibilidad.
- Una OT puede tener varias intervenciones; cada intervencion pertenece a una asignacion.
- Checklist, notas tecnicas, fallas, reparaciones, componentes, evidencia y conformidad se vinculan a una intervencion.
- La conformidad es unica por intervencion. La OT solo puede completarse con intervencion finalizada y conformidad aceptada.
- El historial de mantenimiento es una consulta o vista derivada, no una tabla fuente duplicada.
- Los registros con historial no se eliminan fisicamente; se desactivan o cambian de estado.
- Las relaciones deben validarse: equipo dentro de la sede de la OT, componente dentro del equipo de la OT y tecnico con rol `TECHNICIAN`.

## 10. Estado de implementacion MVP

| Area | Endpoints principales | Estado |
|---|---|---|
| Autenticacion | `/auth/login`, `/auth/logout`, `/auth/me` | Pendiente |
| Usuarios y roles | `/users` | Pendiente |
| Clientes y activos | `/clients`, `/sites`, `/installations`, `/equipment` | Pendiente |
| Solicitudes y OT | `/service-requests`, `/service-types`, `/work-orders` | Pendiente |
| Planificacion | `/technicians`, `/availability`, `/assignments`, `/calendar` | Pendiente |
| Ejecucion | `/interventions`, `/failures`, `/repairs`, `/components` | Pendiente |
| Checklist y evidencia | `/checklists`, `/checklist-responses`, `/technical-notes`, `/evidence` | Pendiente |
| Conformidad e historial | `/conformity`, `/maintenance-history` | Pendiente |
| Preventivo | `/preventive-maintenance-plans` | Pendiente |

`API.md` debe mantenerse alineado con `modules-and-entities.md` y con la implementacion OpenAPI generada por el backend.
