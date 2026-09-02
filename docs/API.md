# API.md - Contrato de API de FieldFlow

**Este documento es el contrato entre el backend y el frontend.** Define la API REST v1 para administrar, planificar, ejecutar y dar trazabilidad a los servicios técnicos en campo. El backend y el frontend deben mantenerse alineados con este documento.

- **Version:** v1
- **Base URL local:** `http://localhost:8080/api/v1`
- **Formato:** JSON, salvo carga de archivos de evidencia
- **Especificacion viva:** `http://localhost:8080/v3/api-docs`

## Convenciones generales

| Aspecto | Regla |
|---|---|
| Nombres JSON | `camelCase` |
| Fechas | ISO-8601 con zona, siempre UTC. Ejemplo: `2026-09-02T14:30:00Z` |
| Identificadores | UUID como string |
| Nulos | `null` explicito; no se reemplazan por `""` ni se omiten |
| PATCH parcial | Solo los campos enviados se actualizan; los campos no enviados no se interpretan como `null` |
| Paginacion | `?page=0&size=20&sort=createdAt,desc` |
| Orden | `sort=campo,asc` o `sort=campo,desc` |
| Autenticacion | `Authorization: Bearer <token>` |
| Idempotencia | `Idempotency-Key: <UUID>` en los POST indicados |
| Respuestas | El cuerpo respeta `Content-Type: application/json; charset=utf-8` |
| Archivos | `multipart/form-data`; el JSON asociado viaja en el campo `metadata` |
| Limite de pagina | `size` entre 1 y 100; valor predeterminado 20 |

### Roles

- La fuente del proyecto distingue explícitamente al área administrativa y a los técnicos. Los únicos valores persistidos en `role.code` son `ADMINISTRATION` y `TECHNICIAN`.
- `ADMINISTRATION`: administra usuarios, catalogos y toda la operacion.
- `TECHNICIAN`: consulta sus trabajos y registra la intervencion, evidencia y conformidad.
- Las etiquetas de autorización `ADMIN`, `DISPATCHER` y `VIEWER` usadas en algunas rutas son perfiles de permisos internos mapeados a `ADMINISTRATION`; no son valores adicionales de `role` ni roles de negocio.

Las rutas autenticadas requieren el rol indicado. Una ruta que opera sobre un recurso restringido devuelve `404` si el recurso no pertenece al alcance del usuario, para no revelar su existencia.

## Formato de error (RFC 9457)

Todos los errores de la API usan `application/problem+json` y esta estructura:

```json
{
  "type": "https://fieldflow.example/errors/validation",
  "title": "Datos de entrada invalidos",
  "status": 400,
  "detail": "Uno o mas campos no cumplen las reglas requeridas",
  "instance": "/api/v1/work-orders",
  "traceId": "0af7651916cd43dd8448eb211c80319c",
  "errors": [
    { "field": "priority", "message": "debe ser uno de: LOW, MEDIUM, HIGH, URGENT" }
  ]
}
```

`errors` es un arreglo (puede ser vacio). Los errores de negocio pueden usar `errors: []`. Los tipos de error son `validation`, `authentication`, `authorization`, `not-found`, `conflict`, `business-rule`, `rate-limit` y `internal`.

## Codigos de estado

`200` OK · `201` Creado · `202` Aceptado · `204` Sin contenido · `400` Entrada invalida · `401` Sin autenticar · `403` Sin permiso · `404` No existe · `409` Conflicto · `422` Regla de negocio violada · `413` Archivo demasiado grande · `415` Tipo de archivo no soportado · `429` Demasiadas peticiones · `500` Error del servidor · `503` Servicio no disponible.

## 1. Autenticacion y usuarios

### `POST /auth/login`

Inicia sesion para un usuario interno. **Roles:** publico.

**Request**

```json
{
  "email": "operador@fieldflow.com",
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
    "name": "Ana Perez",
    "email": "operador@fieldflow.com",
    "role": "ADMINISTRATION",
    "active": true
  }
}
```

**Errores:** `400`, `401`, `429`, `500`.

### `POST /auth/logout`

Invalida el token actual. **Autenticacion:** cualquier rol.

**Request:** sin cuerpo.

**Response `204`:** sin cuerpo.

**Errores:** `401`, `500`.

### `GET /auth/me`

Obtiene el usuario autenticado. **Autenticacion:** cualquier rol.

**Request:** sin cuerpo.

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000001",
  "name": "Ana Perez",
  "email": "operador@fieldflow.com",
  "role": "ADMINISTRATION",
  "active": true
}
```

**Errores:** `401`, `500`.

### `POST /users`

Crea un usuario interno. **Roles:** `ADMIN`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "name": "Luis Gomez",
  "email": "luis@fieldflow.com",
  "password": "secreto",
  "role": "TECHNICIAN"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000002",
  "name": "Luis Gomez",
  "email": "luis@fieldflow.com",
  "role": "TECHNICIAN",
  "active": true,
  "createdAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `400`, `401`, `403`, `409`, `422`, `500`.

## 2. Solicitudes y activos operativos

### `POST /service-requests`

Registra una solicitud o necesidad de mantenimiento recibida por administración. La solicitud es la entrada que puede convertirse en una OT. **Roles:** `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "clientId": "0192f3a1-0000-4000-8000-000000000010",
  "siteId": "0192f3a1-0000-4000-8000-000000000011",
  "equipmentId": "0192f3a1-0000-4000-8000-000000000020",
  "description": "El compresor presenta vibracion inusual",
  "requestedAt": "2026-09-02T14:30:00Z",
  "source": "CUSTOMER"
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
  "requestedAt": "2026-09-02T14:30:00Z",
  "source": "CUSTOMER",
  "status": "RECEIVED",
  "workOrderId": null,
  "createdAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `POST /service-requests/{requestId}/work-order`

Convierte una solicitud recibida en una OT. **Roles:** `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "title": "Revision de compresor",
  "serviceTypeId": "0192f3a1-0000-4000-8000-000000000030",
  "priority": "HIGH",
  "estimatedDurationMinutes": 180,
  "dueAt": "2026-09-10T23:59:59Z",
  "instructions": "Coordinar parada con el responsable de planta"
}
```

**Response `201`**

```json
{
  "requestId": "0192f3a1-0000-4000-8000-000000000005",
  "workOrderId": "0192f3a1-0000-4000-8000-000000000050",
  "workOrderNumber": "OT-000050",
  "status": "PENDING",
  "createdAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

## 3. Clientes, sedes, instalaciones y equipos

### `GET /clients`

Lista clientes con sus sedes resumidas. **Roles:** `ADMIN`, `DISPATCHER`, `VIEWER`; un tecnico solo puede consultar clientes relacionados con sus OT asignadas.

**Request:** sin cuerpo. Query opcional: `search`, `status=ACTIVE|INACTIVE`, `page`, `size`, `sort`.

**Response `200`**

```json
{
  "content": [
    {
      "id": "0192f3a1-0000-4000-8000-000000000010",
      "name": "Acme Industrial",
      "taxId": "30-71234567-8",
      "contact": { "name": "Maria Silva", "email": "maria@acme.com", "phone": "+54 11 5555-0101" },
      "status": "ACTIVE",
      "siteCount": 2
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

**Errores:** `401`, `403`, `400`, `500`.

### `POST /clients`

Crea un cliente. **Roles:** `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "name": "Acme Industrial",
  "taxId": "30-71234567-8",
  "contact": { "name": "Maria Silva", "email": "maria@acme.com", "phone": "+54 11 5555-0101" }
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000010",
  "name": "Acme Industrial",
  "taxId": "30-71234567-8",
  "contact": { "name": "Maria Silva", "email": "maria@acme.com", "phone": "+54 11 5555-0101" },
  "status": "ACTIVE",
  "createdAt": "2026-09-02T14:30:00Z",
  "updatedAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `400`, `401`, `403`, `409`, `422`, `500`.

### `GET /clients/{clientId}`

Devuelve el cliente, sus sedes, instalaciones y el resumen de equipos. **Roles:** `ADMIN`, `DISPATCHER`, `VIEWER`; un tecnico solo puede consultarlo si esta relacionado con una OT asignada.

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000010",
  "name": "Acme Industrial",
  "taxId": "30-71234567-8",
  "contact": { "name": "Maria Silva", "email": "maria@acme.com", "phone": "+54 11 5555-0101" },
  "status": "ACTIVE",
  "sites": [
    { "id": "0192f3a1-0000-4000-8000-000000000011", "name": "Planta Norte", "address": "Av. Central 123", "equipmentCount": 8 }
  ]
}
```

**Errores:** `401`, `403`, `404`, `500`.

### `PATCH /clients/{clientId}`

Actualiza datos del cliente o lo desactiva. **Roles:** `ADMIN`, `DISPATCHER`.

**Request**

```json
{
  "name": "Acme Industrial SA",
  "contact": { "name": "Maria Silva", "email": "maria@acme.com", "phone": "+54 11 5555-0199" },
  "status": "ACTIVE"
}
```

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000010",
  "name": "Acme Industrial SA",
  "taxId": "30-71234567-8",
  "contact": { "name": "Maria Silva", "email": "maria@acme.com", "phone": "+54 11 5555-0199" },
  "status": "ACTIVE",
  "updatedAt": "2026-09-02T14:35:00Z"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `POST /clients/{clientId}/sites`

Registra una sede o instalacion del cliente. **Roles:** `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "name": "Planta Norte",
  "address": "Av. Central 123",
  "city": "Cordoba",
  "country": "AR",
  "latitude": -31.4167,
  "longitude": -64.1833,
  "contact": { "name": "Carlos Ruiz", "phone": "+54 351 5555-0101" },
  "accessNotes": "Ingreso por porteria norte"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000011",
  "clientId": "0192f3a1-0000-4000-8000-000000000010",
  "name": "Planta Norte",
  "address": "Av. Central 123",
  "city": "Cordoba",
  "country": "AR",
  "latitude": -31.4167,
  "longitude": -64.1833,
  "contact": { "name": "Carlos Ruiz", "phone": "+54 351 5555-0101" },
  "accessNotes": "Ingreso por porteria norte",
  "status": "ACTIVE"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `POST /sites/{siteId}/installations`

Registra una instalacion dentro de una sede. La instalacion permite conservar el nivel intermedio entre la ubicacion del cliente y los equipos. **Roles:** `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "name": "Sala de compresores",
  "description": "Area tecnica norte",
  "locationNotes": "Acceso por pasillo lateral"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000012",
  "siteId": "0192f3a1-0000-4000-8000-000000000011",
  "name": "Sala de compresores",
  "description": "Area tecnica norte",
  "locationNotes": "Acceso por pasillo lateral",
  "status": "ACTIVE",
  "createdAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `GET /installations/{installationId}`

Consulta una instalacion y sus equipos. **Roles:** `ADMIN`, `DISPATCHER`, `VIEWER`; un tecnico solo puede consultarla si esta relacionada con una OT asignada.

**Request:** sin cuerpo.

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000012",
  "siteId": "0192f3a1-0000-4000-8000-000000000011",
  "name": "Sala de compresores",
  "description": "Area tecnica norte",
  "locationNotes": "Acceso por pasillo lateral",
  "status": "ACTIVE",
  "equipment": []
}
```

**Errores:** `401`, `403`, `404`, `500`.

### `GET /sites/{siteId}`

Devuelve la sede, sus instalaciones y equipos. **Roles:** `ADMIN`, `DISPATCHER`, `VIEWER`; un tecnico solo puede consultarla si esta relacionada con una OT asignada.

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000011",
  "client": { "id": "0192f3a1-0000-4000-8000-000000000010", "name": "Acme Industrial" },
  "name": "Planta Norte",
  "address": "Av. Central 123",
  "city": "Cordoba",
  "country": "AR",
  "contact": { "name": "Carlos Ruiz", "phone": "+54 351 5555-0101" },
  "accessNotes": "Ingreso por porteria norte",
  "status": "ACTIVE",
  "installations": [],
  "equipment": []
}
```

**Errores:** `401`, `403`, `404`, `500`.

### `POST /sites/{siteId}/equipment`

Registra un equipo en una sede. **Roles:** `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "name": "Compresor principal",
  "installationId": "0192f3a1-0000-4000-8000-000000000012",
  "assetTag": "CMP-001",
  "serialNumber": "SN-88421",
  "model": "AC-500",
  "manufacturer": "IndustrialTech",
  "equipmentType": "COMPRESSOR",
  "installedAt": "2025-04-10T12:00:00Z",
  "criticality": "HIGH",
  "notes": "Requiere parada coordinada"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000020",
  "siteId": "0192f3a1-0000-4000-8000-000000000011",
  "installationId": "0192f3a1-0000-4000-8000-000000000012",
  "name": "Compresor principal",
  "assetTag": "CMP-001",
  "serialNumber": "SN-88421",
  "model": "AC-500",
  "manufacturer": "IndustrialTech",
  "equipmentType": "COMPRESSOR",
  "installedAt": "2025-04-10T12:00:00Z",
  "criticality": "HIGH",
  "status": "OPERATIONAL",
  "createdAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `GET /equipment/{equipmentId}`

Consulta el equipo con su ultimo mantenimiento y resumen de historial. **Roles:** `ADMIN`, `DISPATCHER`, `VIEWER`; un tecnico solo puede consultarlo si esta relacionado con una OT asignada.

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000020",
  "installationId": "0192f3a1-0000-4000-8000-000000000012",
  "client": { "id": "0192f3a1-0000-4000-8000-000000000010", "name": "Acme Industrial" },
  "site": { "id": "0192f3a1-0000-4000-8000-000000000011", "name": "Planta Norte", "address": "Av. Central 123" },
  "name": "Compresor principal",
  "assetTag": "CMP-001",
  "serialNumber": "SN-88421",
  "model": "AC-500",
  "manufacturer": "IndustrialTech",
  "equipmentType": "COMPRESSOR",
  "criticality": "HIGH",
  "status": "OPERATIONAL",
  "lastMaintenanceAt": "2026-08-20T16:00:00Z",
  "openWorkOrderCount": 1
}
```

**Errores:** `401`, `403`, `404`, `500`.

## 3. Catalogos de servicios y checklists

### `GET /service-types`

Lista tipos de servicio. **Roles:** todos los autenticados. Query opcional: `active`, `page`, `size`.

**Response `200`**

```json
{
  "content": [
    {
      "id": "0192f3a1-0000-4000-8000-000000000030",
      "code": "PREVENTIVE_COMPRESSOR",
      "name": "Mantenimiento preventivo de compresor",
      "description": "Inspeccion y mantenimiento programado",
      "estimatedDurationMinutes": 180,
      "requiresChecklist": true,
      "active": true
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

**Errores:** `400`, `401`, `403`, `500`.

### `POST /service-types`

Crea un tipo de servicio. **Roles:** `ADMIN`.

**Request**

```json
{
  "code": "PREVENTIVE_COMPRESSOR",
  "name": "Mantenimiento preventivo de compresor",
  "description": "Inspeccion y mantenimiento programado",
  "estimatedDurationMinutes": 180,
  "requiresChecklist": true
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000030",
  "code": "PREVENTIVE_COMPRESSOR",
  "name": "Mantenimiento preventivo de compresor",
  "description": "Inspeccion y mantenimiento programado",
  "estimatedDurationMinutes": 180,
  "requiresChecklist": true,
  "active": true,
  "createdAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `400`, `401`, `403`, `409`, `422`, `500`.

### `GET /checklists?serviceTypeId={serviceTypeId}`

Obtiene el checklist vigente de un tipo de servicio. **Roles:** todos los autenticados.

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000031",
  "serviceTypeId": "0192f3a1-0000-4000-8000-000000000030",
  "version": 2,
  "title": "Revision de compresor",
  "items": [
    { "id": "0192f3a1-0000-4000-8000-000000000032", "order": 1, "label": "Verificar nivel de aceite", "required": true, "responseType": "BOOLEAN" },
    { "id": "0192f3a1-0000-4000-8000-000000000033", "order": 2, "label": "Registrar presion de salida", "required": true, "responseType": "NUMBER", "unit": "bar" }
  ],
  "active": true
}
```

**Errores:** `401`, `403`, `404`, `500`.

### `POST /checklists`

Crea una version de checklist. **Roles:** `ADMIN`.

**Request**

```json
{
  "serviceTypeId": "0192f3a1-0000-4000-8000-000000000030",
  "title": "Revision de compresor",
  "items": [
    { "order": 1, "label": "Verificar nivel de aceite", "required": true, "responseType": "BOOLEAN", "unit": null }
  ]
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000031",
  "serviceTypeId": "0192f3a1-0000-4000-8000-000000000030",
  "version": 3,
  "title": "Revision de compresor",
  "items": [
    { "id": "0192f3a1-0000-4000-8000-000000000034", "order": 1, "label": "Verificar nivel de aceite", "required": true, "responseType": "BOOLEAN", "unit": null }
  ],
  "active": true
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

## 4. Tecnicos, disponibilidad y agenda

### `GET /technicians`

Lista tecnicos activos y su disponibilidad resumida. **Roles:** `ADMIN`, `DISPATCHER`, `VIEWER`; `TECHNICIAN` puede consultar su propio registro.

**Request:** sin cuerpo. Query opcional: `availableFrom`, `availableTo`, `serviceTypeId`, `page`, `size`.

**Response `200`**

```json
{
  "content": [
    {
      "id": "0192f3a1-0000-4000-8000-000000000040",
      "name": "Luis Gomez",
      "email": "luis@fieldflow.com",
      "phone": "+54 351 5555-0111",
      "specialties": ["COMPRESSOR", "ELECTRICAL"],
      "status": "ACTIVE",
      "available": true
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

**Errores:** `400`, `401`, `403`, `500`.

### `PUT /technicians/{technicianId}/availability`

Reemplaza la disponibilidad de un tecnico para un periodo. **Roles:** `ADMIN`, `DISPATCHER`; un `TECHNICIAN` puede modificar solo la propia.

**Request**

```json
{
  "from": "2026-09-07T00:00:00Z",
  "to": "2026-09-13T23:59:59Z",
  "slots": [
    { "startsAt": "2026-09-08T09:00:00Z", "endsAt": "2026-09-08T17:00:00Z", "status": "AVAILABLE" },
    { "startsAt": "2026-09-10T09:00:00Z", "endsAt": "2026-09-10T17:00:00Z", "status": "UNAVAILABLE", "reason": "Licencia" }
  ]
}
```

**Response `200`**

```json
{
  "technicianId": "0192f3a1-0000-4000-8000-000000000040",
  "from": "2026-09-07T00:00:00Z",
  "to": "2026-09-13T23:59:59Z",
  "slots": [
    { "startsAt": "2026-09-08T09:00:00Z", "endsAt": "2026-09-08T17:00:00Z", "status": "AVAILABLE", "reason": null },
    { "startsAt": "2026-09-10T09:00:00Z", "endsAt": "2026-09-10T17:00:00Z", "status": "UNAVAILABLE", "reason": "Licencia" }
  ]
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `GET /calendar`

Consulta la agenda de trabajos asignados. **Roles:** todos los autenticados; un tecnico solo ve su agenda.

**Request:** sin cuerpo. Query requerida: `from`, `to`; opcional: `technicianId`, `status`.

**Response `200`**

```json
{
  "from": "2026-09-07T00:00:00Z",
  "to": "2026-09-13T23:59:59Z",
  "events": [
    {
      "workOrderId": "0192f3a1-0000-4000-8000-000000000050",
      "title": "OT-000050 - Mantenimiento preventivo",
      "startsAt": "2026-09-08T09:00:00Z",
      "endsAt": "2026-09-08T12:00:00Z",
      "technician": { "id": "0192f3a1-0000-4000-8000-000000000040", "name": "Luis Gomez" },
      "status": "ASSIGNED",
      "site": { "id": "0192f3a1-0000-4000-8000-000000000011", "name": "Planta Norte" }
    }
  ]
}
```

**Errores:** `400`, `401`, `403`, `500`.

## 5. Ordenes de trabajo

### `GET /work-orders`

Lista y filtra OT. **Roles:** todos los autenticados; tecnicos solo ven sus asignaciones.

**Request:** sin cuerpo. Query opcional: `status`, `priority`, `clientId`, `siteId`, `equipmentId`, `technicianId`, `from`, `to`, `page`, `size`, `sort`.

**Response `200`**

```json
{
  "content": [
    {
      "id": "0192f3a1-0000-4000-8000-000000000050",
      "number": "OT-000050",
      "title": "Mantenimiento preventivo",
      "status": "ASSIGNED",
      "priority": "HIGH",
      "plannedStartAt": "2026-09-08T09:00:00Z",
      "plannedEndAt": "2026-09-08T12:00:00Z",
      "client": { "id": "0192f3a1-0000-4000-8000-000000000010", "name": "Acme Industrial" },
      "site": { "id": "0192f3a1-0000-4000-8000-000000000011", "name": "Planta Norte" },
      "equipment": { "id": "0192f3a1-0000-4000-8000-000000000020", "name": "Compresor principal" },
      "technician": { "id": "0192f3a1-0000-4000-8000-000000000040", "name": "Luis Gomez" }
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

**Errores:** `400`, `401`, `403`, `500`.

### `POST /work-orders`

Crea una OT pendiente. **Roles:** `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "title": "Mantenimiento preventivo",
  "description": "Realizar inspeccion general y medir presion",
  "clientId": "0192f3a1-0000-4000-8000-000000000010",
  "siteId": "0192f3a1-0000-4000-8000-000000000011",
  "equipmentId": "0192f3a1-0000-4000-8000-000000000020",
  "installationId": "0192f3a1-0000-4000-8000-000000000012",
  "serviceTypeId": "0192f3a1-0000-4000-8000-000000000030",
  "priority": "HIGH",
  "estimatedDurationMinutes": 180,
  "requestedAt": "2026-09-02T14:30:00Z",
  "dueAt": "2026-09-15T23:59:59Z",
  "instructions": "Coordinar parada con el responsable de planta"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000050",
  "number": "OT-000050",
  "title": "Mantenimiento preventivo",
  "description": "Realizar inspeccion general y medir presion",
  "status": "PENDING",
  "priority": "HIGH",
  "estimatedDurationMinutes": 180,
  "requestedAt": "2026-09-02T14:30:00Z",
  "dueAt": "2026-09-15T23:59:59Z",
  "client": { "id": "0192f3a1-0000-4000-8000-000000000010", "name": "Acme Industrial" },
  "site": { "id": "0192f3a1-0000-4000-8000-000000000011", "name": "Planta Norte" },
  "equipment": { "id": "0192f3a1-0000-4000-8000-000000000020", "name": "Compresor principal" },
  "serviceType": { "id": "0192f3a1-0000-4000-8000-000000000030", "name": "Mantenimiento preventivo de compresor" },
  "technician": null,
  "createdAt": "2026-09-02T14:30:00Z",
  "updatedAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `GET /work-orders/{workOrderId}`

Devuelve la OT completa, asignacion, intervencion, evidencia, conformidad e historial relacionado. **Roles:** todos los autenticados con alcance sobre la OT.

**Request:** sin cuerpo.

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000050",
  "number": "OT-000050",
  "title": "Mantenimiento preventivo",
  "description": "Realizar inspeccion general y medir presion",
  "status": "ASSIGNED",
  "priority": "HIGH",
  "client": { "id": "0192f3a1-0000-4000-8000-000000000010", "name": "Acme Industrial", "contact": { "name": "Maria Silva", "phone": "+54 11 5555-0101" } },
  "site": { "id": "0192f3a1-0000-4000-8000-000000000011", "name": "Planta Norte", "address": "Av. Central 123", "accessNotes": "Ingreso por porteria norte" },
  "equipment": { "id": "0192f3a1-0000-4000-8000-000000000020", "name": "Compresor principal", "assetTag": "CMP-001", "serialNumber": "SN-88421" },
  "serviceType": { "id": "0192f3a1-0000-4000-8000-000000000030", "name": "Mantenimiento preventivo de compresor" },
  "technician": { "id": "0192f3a1-0000-4000-8000-000000000040", "name": "Luis Gomez", "phone": "+54 351 5555-0111" },
  "instructions": "Coordinar parada con el responsable de planta",
  "requestedAt": "2026-09-02T14:30:00Z",
  "dueAt": "2026-09-15T23:59:59Z",
  "plannedStartAt": "2026-09-08T09:00:00Z",
  "plannedEndAt": "2026-09-08T12:00:00Z",
  "intervention": {
    "id": "0192f3a1-0000-4000-8000-000000000060",
    "startedAt": "2026-09-08T09:12:00Z",
    "endedAt": null,
    "status": "IN_PROGRESS",
    "technicalNotes": null,
    "observations": null,
    "measurements": [],
    "failures": [],
    "repairs": [],
    "componentsIntervened": []
  },
  "checklist": null,
  "evidence": [],
  "conformity": null,
  "maintenanceHistory": [],
  "assignmentHistory": [
    {
      "id": "0192f3a1-0000-4000-8000-000000000051",
      "technician": { "id": "0192f3a1-0000-4000-8000-000000000040", "name": "Luis Gomez" },
      "plannedStartAt": "2026-09-08T09:00:00Z",
      "plannedEndAt": "2026-09-08T12:00:00Z",
      "status": "ASSIGNED"
    }
  ],
  "createdAt": "2026-09-02T14:30:00Z",
  "updatedAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `401`, `403`, `404`, `500`.

### `PATCH /work-orders/{workOrderId}`

Actualiza datos editables de una OT pendiente o asignada. **Roles:** `ADMIN`, `DISPATCHER`.

**Request**

```json
{
  "priority": "URGENT",
  "description": "Realizar inspeccion general y medir presion. Llevar repuesto R-22",
  "dueAt": "2026-09-10T23:59:59Z"
}
```

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000050",
  "number": "OT-000050",
  "title": "Mantenimiento preventivo",
  "description": "Realizar inspeccion general y medir presion. Llevar repuesto R-22",
  "status": "PENDING",
  "priority": "URGENT",
  "dueAt": "2026-09-10T23:59:59Z",
  "updatedAt": "2026-09-02T14:35:00Z"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `POST /work-orders/{workOrderId}/assignments`

Asigna tecnico y horario a una OT. **Roles:** `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "technicianId": "0192f3a1-0000-4000-8000-000000000040",
  "plannedStartAt": "2026-09-08T09:00:00Z",
  "plannedEndAt": "2026-09-08T12:00:00Z",
  "notes": "Confirmado con el cliente"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000051",
  "workOrderId": "0192f3a1-0000-4000-8000-000000000050",
  "technician": { "id": "0192f3a1-0000-4000-8000-000000000040", "name": "Luis Gomez" },
  "plannedStartAt": "2026-09-08T09:00:00Z",
  "plannedEndAt": "2026-09-08T12:00:00Z",
  "status": "ACTIVE",
  "notes": "Confirmado con el cliente",
  "createdAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `PATCH /work-orders/{workOrderId}/status`

Cambia el estado de la OT respetando las transiciones permitidas. **Roles:** `ADMIN`, `DISPATCHER`; `TECHNICIAN` puede usar solo `EN_ROUTE` en sus OT. `IN_PROGRESS` se establece al iniciar la intervención, `PENDING_CUSTOMER_CONFIRMATION` al completarla y `COMPLETED` solo mediante conformidad aceptada.

Estados: `PENDING`, `ASSIGNED`, `EN_ROUTE`, `IN_PROGRESS`, `PENDING_CUSTOMER_CONFIRMATION`, `COMPLETED` y `RESCHEDULED`. Son los estados definidos en la fuente del proyecto; `CANCELLED` no se incorpora a v1.

**Request**

```json
{
  "status": "EN_ROUTE",
  "reason": null,
  "rescheduledStart": null,
  "rescheduledEnd": null
}
```

Cuando `status` es `RESCHEDULED`, `reason`, `rescheduledStart` y `rescheduledEnd` son obligatorios; para los demas estados deben ser `null`. La respuesta devuelve los valores efectivos y conserva la asignacion anterior en `assignmentHistory`.

**Response `200`**

```json
{
  "workOrderId": "0192f3a1-0000-4000-8000-000000000050",
  "previousStatus": "ASSIGNED",
  "status": "EN_ROUTE",
  "rescheduledStart": null,
  "rescheduledEnd": null,
  "changedAt": "2026-09-08T08:35:00Z",
  "changedBy": { "id": "0192f3a1-0000-4000-8000-000000000040", "name": "Luis Gomez" }
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

## 6. Intervencion y evidencia en campo

### `POST /work-orders/{workOrderId}/intervention`

Inicia o registra la intervencion de la OT. **Roles:** `TECHNICIAN` asignado, `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "startedAt": "2026-09-08T09:12:00Z",
  "initialNotes": "Equipo detenido al llegar; se inicia inspeccion"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000060",
  "workOrderId": "0192f3a1-0000-4000-8000-000000000050",
  "startedAt": "2026-09-08T09:12:00Z",
  "endedAt": null,
  "status": "IN_PROGRESS",
  "initialNotes": "Equipo detenido al llegar; se inicia inspeccion",
  "technicalNotes": null,
  "measurements": [],
  "checklist": null
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `PATCH /work-orders/{workOrderId}/intervention`

Completa el registro tecnico de la intervencion. La OT queda pendiente de conformidad y no se considera finalizada. **Roles:** tecnico asignado, `ADMIN`, `DISPATCHER`.

**Request**

```json
{
  "endedAt": "2026-09-08T11:45:00Z",
  "technicalNotes": "Se ajusto correa y se reemplazo filtro. Equipo operativo.",
  "observations": "Vibracion dentro del rango luego del ajuste",
  "measurements": [
    { "name": "Presion de salida", "value": 8.4, "unit": "bar" }
  ],
  "failures": [
    { "description": "Correa floja", "severity": "MEDIUM", "detectedAt": "2026-09-08T09:30:00Z", "resolved": true }
  ],
  "repairs": [
    { "description": "Ajuste de correa", "status": "COMPLETED", "completedAt": "2026-09-08T10:30:00Z", "notes": null },
    { "description": "Reemplazo de filtro", "status": "COMPLETED", "completedAt": "2026-09-08T10:45:00Z", "notes": null }
  ],
  "componentsIntervened": [
    { "equipmentComponentId": "0192f3a1-0000-4000-8000-000000000071", "action": "REPAIRED", "description": "Ajuste de correa" },
    { "equipmentComponentId": "0192f3a1-0000-4000-8000-000000000072", "action": "REPLACED", "description": "Reemplazo de filtro" }
  ],
  "equipmentStatus": "OPERATIONAL",
  "result": "COMPLETED"
}
```

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000060",
  "workOrderId": "0192f3a1-0000-4000-8000-000000000050",
  "startedAt": "2026-09-08T09:12:00Z",
  "endedAt": "2026-09-08T11:45:00Z",
  "status": "PENDING_CUSTOMER_CONFIRMATION",
  "technicalNotes": "Se ajusto correa y se reemplazo filtro. Equipo operativo.",
  "observations": "Vibracion dentro del rango luego del ajuste",
  "measurements": [
    { "name": "Presion de salida", "value": 8.4, "unit": "bar" }
  ],
  "failures": [
    { "description": "Correa floja", "severity": "MEDIUM", "detectedAt": "2026-09-08T09:30:00Z", "resolved": true }
  ],
  "repairs": [
    { "description": "Ajuste de correa", "status": "COMPLETED", "completedAt": "2026-09-08T10:30:00Z", "notes": null },
    { "description": "Reemplazo de filtro", "status": "COMPLETED", "completedAt": "2026-09-08T10:45:00Z", "notes": null }
  ],
  "componentsIntervened": [
    { "equipmentComponentId": "0192f3a1-0000-4000-8000-000000000071", "action": "REPAIRED", "description": "Ajuste de correa" },
    { "equipmentComponentId": "0192f3a1-0000-4000-8000-000000000072", "action": "REPLACED", "description": "Reemplazo de filtro" }
  ],
  "equipmentStatus": "OPERATIONAL",
  "result": "COMPLETED"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `PUT /work-orders/{workOrderId}/checklist`

Guarda las respuestas del checklist asociado al servicio. Permite guardado parcial. **Roles:** tecnico asignado, `ADMIN`, `DISPATCHER`.

**Request**

```json
{
  "checklistId": "0192f3a1-0000-4000-8000-000000000031",
  "answers": [
    { "itemId": "0192f3a1-0000-4000-8000-000000000032", "value": true, "note": null },
    { "itemId": "0192f3a1-0000-4000-8000-000000000033", "value": 8.4, "note": "Dentro de rango" }
  ],
  "completed": true
}
```

**Response `200`**

```json
{
  "checklistId": "0192f3a1-0000-4000-8000-000000000031",
  "workOrderId": "0192f3a1-0000-4000-8000-000000000050",
  "answeredCount": 2,
  "totalItems": 2,
  "completed": true,
  "answers": [
    { "itemId": "0192f3a1-0000-4000-8000-000000000032", "value": true, "note": null },
    { "itemId": "0192f3a1-0000-4000-8000-000000000033", "value": 8.4, "note": "Dentro de rango" }
  ]
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `POST /work-orders/{workOrderId}/evidence`

Carga una foto o archivo de evidencia. **Roles:** tecnico asignado, `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido. `Content-Type: multipart/form-data`.

**Request**

Campo `file`: binario. Campo `metadata`:

```json
{
  "type": "PHOTO",
  "caption": "Filtro reemplazado",
  "takenAt": "2026-09-08T10:40:00Z"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000061",
  "workOrderId": "0192f3a1-0000-4000-8000-000000000050",
  "type": "PHOTO",
  "fileName": "filtro-reemplazado.jpg",
  "contentType": "image/jpeg",
  "sizeBytes": 248912,
  "caption": "Filtro reemplazado",
  "takenAt": "2026-09-08T10:40:00Z",
  "url": "/api/v1/evidence/0192f3a1-0000-4000-8000-000000000061/download",
  "createdAt": "2026-09-08T10:41:00Z"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `413`, `415`, `422`, `500`, `503`.

### `GET /work-orders/{workOrderId}/evidence`

Lista la evidencia de una OT. **Roles:** todos los usuarios con alcance sobre la OT.

**Response `200`**

```json
{
  "workOrderId": "0192f3a1-0000-4000-8000-000000000050",
  "items": [
    {
      "id": "0192f3a1-0000-4000-8000-000000000061",
      "type": "PHOTO",
      "fileName": "filtro-reemplazado.jpg",
      "contentType": "image/jpeg",
      "sizeBytes": 248912,
      "caption": "Filtro reemplazado",
      "url": "/api/v1/evidence/0192f3a1-0000-4000-8000-000000000061/download",
      "createdAt": "2026-09-08T10:41:00Z"
    }
  ]
}
```

**Errores:** `401`, `403`, `404`, `500`.

### `GET /evidence/{evidenceId}/download`

Descarga la evidencia. **Roles:** todos los usuarios con alcance sobre la OT. La respuesta es binaria con `Content-Disposition: attachment`.

**Request:** sin cuerpo.

**Respuesta `200`:** archivo binario.

**Errores:** `401`, `403`, `404`, `500`, `503`.

## 7. Conformidad e historial

### `POST /work-orders/{workOrderId}/conformity`

Registra la firma o conformidad del cliente. Solo `accepted: true` lleva la OT a `COMPLETED`; con `accepted: false` permanece en `PENDING_CUSTOMER_CONFIRMATION`. **Roles:** `TECHNICIAN` asignado, `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "accepted": true,
  "customerName": "Carlos Ruiz",
  "customerRole": "Responsable de planta",
  "signatureData": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUg...",
  "comments": "Trabajo realizado conforme"
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000062",
  "workOrderId": "0192f3a1-0000-4000-8000-000000000050",
  "accepted": true,
  "customerName": "Carlos Ruiz",
  "customerRole": "Responsable de planta",
  "comments": "Trabajo realizado conforme",
  "signedAt": "2026-09-08T12:00:00Z",
  "workOrderStatus": "COMPLETED",
  "equipmentStatus": "OPERATIONAL"
}
```

Si `accepted` es `false`, el mismo response devuelve `workOrderStatus: "PENDING_CUSTOMER_CONFIRMATION"`; `comments` debe explicar la disconformidad.

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `GET /equipment/{equipmentId}/maintenance-history`

Consulta el historial completo de mantenimientos, fallas y reparaciones del equipo. **Roles:** `ADMIN`, `DISPATCHER`, `VIEWER`; un tecnico solo puede consultarlo si el equipo esta relacionado con una OT asignada.

**Request:** sin cuerpo. Query opcional: `from`, `to`, `type`, `page`, `size`, `sort`.

**Response `200`**

```json
{
  "equipmentId": "0192f3a1-0000-4000-8000-000000000020",
  "content": [
    {
      "workOrderId": "0192f3a1-0000-4000-8000-000000000050",
      "number": "OT-000050",
      "serviceType": "Mantenimiento preventivo de compresor",
      "type": "PREVENTIVE",
      "status": "COMPLETED",
      "performedAt": "2026-09-08T11:45:00Z",
      "technician": { "id": "0192f3a1-0000-4000-8000-000000000040", "name": "Luis Gomez" },
      "summary": "Se ajusto correa y se reemplazo filtro. Equipo operativo.",
      "failures": [
        { "description": "Correa floja", "severity": "MEDIUM", "detectedAt": "2026-09-08T09:30:00Z", "resolved": true }
      ],
      "repairs": [
        { "description": "Ajuste de correa", "status": "COMPLETED", "completedAt": "2026-09-08T10:30:00Z", "notes": null },
        { "description": "Reemplazo de filtro", "status": "COMPLETED", "completedAt": "2026-09-08T10:45:00Z", "notes": null }
      ],
      "componentsIntervened": [
        { "equipmentComponentId": "0192f3a1-0000-4000-8000-000000000071", "action": "REPAIRED", "description": "Ajuste de correa" },
        { "equipmentComponentId": "0192f3a1-0000-4000-8000-000000000072", "action": "REPLACED", "description": "Reemplazo de filtro" }
      ],
      "equipmentStatus": "OPERATIONAL",
      "evidenceCount": 2,
      "conformityAccepted": true
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

**Errores:** `400`, `401`, `403`, `404`, `500`.

## 8. Mantenimiento preventivo y recurrencias

### `GET /preventive-maintenance-plans`

Lista planes preventivos. **Roles:** `ADMIN`, `DISPATCHER`, `VIEWER`.

**Request:** sin cuerpo. Query opcional: `equipmentId`, `active`, `dueBefore`, `page`, `size`.

**Response `200`**

```json
{
  "content": [
    {
      "id": "0192f3a1-0000-4000-8000-000000000070",
      "equipmentId": "0192f3a1-0000-4000-8000-000000000020",
      "serviceTypeId": "0192f3a1-0000-4000-8000-000000000030",
      "frequency": "EVERY_3_MONTHS",
      "nextDueAt": "2026-11-20T09:00:00Z",
      "active": true,
      "autoCreateWorkOrder": true
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

**Errores:** `400`, `401`, `403`, `500`.

### `POST /preventive-maintenance-plans`

Crea un plan preventivo recurrente. **Roles:** `ADMIN`, `DISPATCHER`. Header `Idempotency-Key` requerido.

**Request**

```json
{
  "equipmentId": "0192f3a1-0000-4000-8000-000000000020",
  "serviceTypeId": "0192f3a1-0000-4000-8000-000000000030",
  "frequency": "EVERY_3_MONTHS",
  "startsAt": "2026-08-20T09:00:00Z",
  "nextDueAt": "2026-11-20T09:00:00Z",
  "priority": "MEDIUM",
  "preferredTechnicianId": null,
  "autoCreateWorkOrder": true,
  "active": true
}
```

**Response `201`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000070",
  "equipmentId": "0192f3a1-0000-4000-8000-000000000020",
  "serviceTypeId": "0192f3a1-0000-4000-8000-000000000030",
  "frequency": "EVERY_3_MONTHS",
  "startsAt": "2026-08-20T09:00:00Z",
  "nextDueAt": "2026-11-20T09:00:00Z",
  "priority": "MEDIUM",
  "preferredTechnicianId": null,
  "autoCreateWorkOrder": true,
  "active": true,
  "createdAt": "2026-09-02T14:30:00Z"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

### `PATCH /preventive-maintenance-plans/{planId}`

Actualiza, pausa o reactiva un plan preventivo. **Roles:** `ADMIN`, `DISPATCHER`.

**Request**

```json
{
  "nextDueAt": "2026-12-01T09:00:00Z",
  "active": false,
  "autoCreateWorkOrder": true
}
```

**Response `200`**

```json
{
  "id": "0192f3a1-0000-4000-8000-000000000070",
  "equipmentId": "0192f3a1-0000-4000-8000-000000000020",
  "serviceTypeId": "0192f3a1-0000-4000-8000-000000000030",
  "frequency": "EVERY_3_MONTHS",
  "nextDueAt": "2026-12-01T09:00:00Z",
  "active": false,
  "autoCreateWorkOrder": true,
  "updatedAt": "2026-09-02T14:35:00Z"
}
```

**Errores:** `400`, `401`, `403`, `404`, `409`, `422`, `500`.

## 9. Operacion

### `GET /public/ping`

Comprueba que el proceso responde. No consulta la base de datos. **Roles:** publico.

**Response `200`**

```json
{
  "status": "UP",
  "timestamp": "2026-09-02T14:30:00Z"
}
```

**Errores:** `500`.

### `GET /health`

Healthcheck para monitoreo. **Roles:** publico.

**Response `200`**

```json
{
  "status": "UP",
  "checks": {
    "database": "UP",
    "storage": "UP"
  },
  "timestamp": "2026-09-02T14:30:00Z"
}
```

**Errores:** `503`, `500`.

## Estados y reglas de negocio

- Una OT se crea en `PENDING` y solo puede pasar a `ASSIGNED` cuando existe un tecnico disponible y una franja horaria valida.
- `EN_ROUTE` e `IN_PROGRESS` requieren un tecnico asignado.
- `PENDING_CUSTOMER_CONFIRMATION` requiere intervencion registrada; `COMPLETED` requiere intervencion finalizada y conformidad aceptada. Una conformidad no aceptada mantiene la OT en `PENDING_CUSTOMER_CONFIRMATION` y conserva los comentarios.
- El endpoint de cambio de estado no permite saltar las operaciones de intervención y conformidad que producen `IN_PROGRESS`, `PENDING_CUSTOMER_CONFIRMATION` y `COMPLETED`.
- Una OT puede pasar a `RESCHEDULED` indicando `reason`, `rescheduledStart` y `rescheduledEnd`; la asignacion anterior se conserva en el historial y la nueva agenda debe crear una asignacion nueva.
- Una OT `COMPLETED` no puede editarse, excepto para agregar evidencia o corregir datos mediante una accion autorizada de auditoria definida por el backend.
- Un checklist con items obligatorios no puede marcarse como `completed` mientras falte una respuesta.
- El historial de mantenimiento se construye a partir de OT completadas asociadas al equipo; las OT pendientes no se consideran mantenimiento realizado.
- Un plan preventivo activo puede generar una OT cuando alcanza `nextDueAt`; la generacion debe ser idempotente.
- Todas las mutaciones deben registrar actor, fecha, estado anterior y estado nuevo en una bitacora de auditoria, aunque esa bitacora no se expone como endpoint en esta version.
