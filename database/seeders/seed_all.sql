-- FieldFlow - Todos los seeders de ejemplo
-- Ejecutar sobre PostgreSQL/Supabase después de crear el esquema.
-- No forma parte de Flyway.

BEGIN;

-- ============================================================
-- 01_client.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Clientes
-- Tabla: client
-- Dependencias: ninguna
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO client (id, name)
VALUES
    ('01000000-0000-4000-8000-000000000001', 'ACME Industrial'),
    ('01000000-0000-4000-8000-000000000002', 'Servicios Andinos')
ON CONFLICT DO NOTHING;


-- ============================================================
-- 02_site.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Sedes
-- Tabla: site
-- Dependencias: client
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO site (id, client_id, name, address)
VALUES
    (
        '02000000-0000-4000-8000-000000000001',
        '01000000-0000-4000-8000-000000000001',
        'Planta Córdoba',
        'Av. Circunvalación 4500, Córdoba, Argentina'
    ),
    (
        '02000000-0000-4000-8000-000000000002',
        '01000000-0000-4000-8000-000000000001',
        'Depósito Villa María',
        'Ruta Nacional 9 Km 555, Villa María, Córdoba, Argentina'
    ),
    (
        '02000000-0000-4000-8000-000000000003',
        '01000000-0000-4000-8000-000000000002',
        'Centro Operativo Mendoza',
        'Acceso Sur 2100, Godoy Cruz, Mendoza, Argentina'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 03_installation.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Instalaciones
-- Tabla: installation
-- Dependencias: site
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO installation (id, site_id, name)
VALUES
    (
        '03000000-0000-4000-8000-000000000001',
        '02000000-0000-4000-8000-000000000001',
        'Sala de máquinas'
    ),
    (
        '03000000-0000-4000-8000-000000000002',
        '02000000-0000-4000-8000-000000000001',
        'Línea de producción A'
    ),
    (
        '03000000-0000-4000-8000-000000000003',
        '02000000-0000-4000-8000-000000000003',
        'Taller de mantenimiento'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 04_equipment.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Equipos
-- Tabla: equipment
-- Dependencias: site, installation
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO equipment (
    id, site_id, installation_id, identifier, name, current_status
)
VALUES
    (
        '04000000-0000-4000-8000-000000000001',
        '02000000-0000-4000-8000-000000000001',
        '03000000-0000-4000-8000-000000000001',
        'EQ-001',
        'Compresor principal',
        'OPERATIONAL'
    ),
    (
        '04000000-0000-4000-8000-000000000002',
        '02000000-0000-4000-8000-000000000001',
        '03000000-0000-4000-8000-000000000002',
        'EQ-002',
        'Bomba centrífuga',
        'REQUIRES_ATTENTION'
    ),
    (
        '04000000-0000-4000-8000-000000000003',
        '02000000-0000-4000-8000-000000000002',
        NULL,
        'EQ-003',
        'Generador de respaldo',
        'OPERATIONAL'
    ),
    (
        '04000000-0000-4000-8000-000000000004',
        '02000000-0000-4000-8000-000000000003',
        '03000000-0000-4000-8000-000000000003',
        'EQ-004',
        'Unidad HVAC',
        'OPERATIONAL'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 05_service_type.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Tipos de servicio
-- Tabla: service_type
-- Dependencias: ninguna
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO service_type (id, name)
VALUES
    ('05000000-0000-4000-8000-000000000001', 'Mantenimiento preventivo'),
    ('05000000-0000-4000-8000-000000000002', 'Reparación correctiva'),
    ('05000000-0000-4000-8000-000000000003', 'Inspección técnica')
ON CONFLICT DO NOTHING;


-- ============================================================
-- 06_technician.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Técnicos
-- Tabla: technician
-- Dependencias: ninguna
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO technician (id, name)
VALUES
    ('06000000-0000-4000-8000-000000000001', 'Juan Pérez'),
    ('06000000-0000-4000-8000-000000000002', 'Lucía Gómez'),
    ('06000000-0000-4000-8000-000000000003', 'Martín Díaz')
ON CONFLICT DO NOTHING;


-- ============================================================
-- 07_technician_availability.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Disponibilidad de técnicos
-- Tabla: technician_availability
-- Dependencias: technician
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO technician_availability (
    id, technician_id, starts_at, ends_at
)
VALUES
    (
        '07000000-0000-4000-8000-000000000001',
        '06000000-0000-4000-8000-000000000001',
        '2026-09-08T08:00:00-03:00',
        '2026-09-08T17:00:00-03:00'
    ),
    (
        '07000000-0000-4000-8000-000000000002',
        '06000000-0000-4000-8000-000000000002',
        '2026-09-08T08:00:00-03:00',
        '2026-09-08T17:00:00-03:00'
    ),
    (
        '07000000-0000-4000-8000-000000000003',
        '06000000-0000-4000-8000-000000000001',
        '2026-09-09T08:00:00-03:00',
        '2026-09-09T17:00:00-03:00'
    ),
    (
        '07000000-0000-4000-8000-000000000004',
        '06000000-0000-4000-8000-000000000003',
        '2026-09-09T09:00:00-03:00',
        '2026-09-09T18:00:00-03:00'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 08_work_order.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Órdenes de trabajo
-- Tabla: work_order
-- Dependencias: equipment, service_type
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO work_order (
    id,
    equipment_id,
    service_type_id,
    instructions,
    priority,
    estimated_duration,
    status
)
VALUES
    (
        '08000000-0000-4000-8000-000000000001',
        '04000000-0000-4000-8000-000000000001',
        '05000000-0000-4000-8000-000000000001',
        'Realizar mantenimiento preventivo, controlar vibración, temperatura y nivel de aceite.',
        'HIGH',
        90,
        'COMPLETED'
    ),
    (
        '08000000-0000-4000-8000-000000000002',
        '04000000-0000-4000-8000-000000000002',
        '05000000-0000-4000-8000-000000000002',
        'Revisar pérdida de presión y determinar causa de funcionamiento irregular.',
        'HIGH',
        120,
        'ASSIGNED'
    ),
    (
        '08000000-0000-4000-8000-000000000003',
        '04000000-0000-4000-8000-000000000003',
        '05000000-0000-4000-8000-000000000003',
        'Inspeccionar estado general del generador y registrar observaciones.',
        'MEDIUM',
        60,
        'PENDING'
    ),
    (
        '08000000-0000-4000-8000-000000000004',
        '04000000-0000-4000-8000-000000000004',
        '05000000-0000-4000-8000-000000000001',
        'Realizar mantenimiento preventivo del sistema HVAC y verificar filtros.',
        'MEDIUM',
        90,
        'PENDING_CUSTOMER_CONFIRMATION'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 09_assignment.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Asignaciones
-- Tabla: assignment
-- Dependencias: work_order, technician, technician_availability
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO assignment (
    id, work_order_id, technician_id, planned_start_at, planned_end_at
)
VALUES
    (
        '09000000-0000-4000-8000-000000000001',
        '08000000-0000-4000-8000-000000000001',
        '06000000-0000-4000-8000-000000000001',
        '2026-09-08T09:00:00-03:00',
        '2026-09-08T10:30:00-03:00'
    ),
    (
        '09000000-0000-4000-8000-000000000002',
        '08000000-0000-4000-8000-000000000002',
        '06000000-0000-4000-8000-000000000002',
        '2026-09-08T13:00:00-03:00',
        '2026-09-08T15:00:00-03:00'
    ),
    (
        '09000000-0000-4000-8000-000000000003',
        '08000000-0000-4000-8000-000000000004',
        '06000000-0000-4000-8000-000000000003',
        '2026-09-09T10:00:00-03:00',
        '2026-09-09T11:30:00-03:00'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 10_checklist.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Checklists
-- Tabla: checklist
-- Dependencias: work_order
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO checklist (id, work_order_id, name)
VALUES
    (
        '0a000000-0000-4000-8000-000000000001',
        '08000000-0000-4000-8000-000000000001',
        'Checklist preventivo - Compresor'
    ),
    (
        '0a000000-0000-4000-8000-000000000002',
        '08000000-0000-4000-8000-000000000004',
        'Checklist preventivo - HVAC'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 11_checklist_item.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Ítems de checklist
-- Tabla: checklist_item
-- Dependencias: checklist
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO checklist_item (id, checklist_id, label)
VALUES
    (
        '0b000000-0000-4000-8000-000000000001',
        '0a000000-0000-4000-8000-000000000001',
        'Verificar nivel de aceite'
    ),
    (
        '0b000000-0000-4000-8000-000000000002',
        '0a000000-0000-4000-8000-000000000001',
        'Verificar temperatura y vibraciones'
    ),
    (
        '0b000000-0000-4000-8000-000000000003',
        '0a000000-0000-4000-8000-000000000002',
        'Verificar estado de filtros'
    ),
    (
        '0b000000-0000-4000-8000-000000000004',
        '0a000000-0000-4000-8000-000000000002',
        'Verificar conexiones eléctricas'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 12_intervention.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Intervenciones
-- Tabla: intervention
-- Dependencias: work_order, technician, assignment
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO intervention (
    id,
    work_order_id,
    technician_id,
    started_at,
    ended_at,
    status,
    result,
    observations
)
VALUES
    (
        '0c000000-0000-4000-8000-000000000001',
        '08000000-0000-4000-8000-000000000001',
        '06000000-0000-4000-8000-000000000001',
        '2026-09-08T09:05:00-03:00',
        '2026-09-08T10:20:00-03:00',
        'COMPLETED',
        'Equipo operativo luego del mantenimiento.',
        'Se recomienda repetir el control de vibraciones en el próximo mantenimiento.'
    ),
    (
        '0c000000-0000-4000-8000-000000000002',
        '08000000-0000-4000-8000-000000000004',
        '06000000-0000-4000-8000-000000000003',
        '2026-09-09T10:05:00-03:00',
        '2026-09-09T11:10:00-03:00',
        'PENDING_CUSTOMER_CONFIRMATION',
        'Mantenimiento realizado correctamente.',
        'Se reemplazó un filtro saturado. Pendiente de conformidad del cliente.'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 13_failure.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Fallas
-- Tabla: failure
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO failure (id, intervention_id, description)
VALUES
    (
        '0d000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'Se detectó vibración leve por fijaciones con ajuste insuficiente.'
    ),
    (
        '0d000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000002',
        'Filtro de retorno con saturación superior a la esperada.'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 14_repair.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Reparaciones
-- Tabla: repair
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO repair (id, intervention_id, description)
VALUES
    (
        '0e000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'Se ajustaron las fijaciones del soporte del motor.'
    ),
    (
        '0e000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000002',
        'Se reemplazó el filtro de retorno y se limpió el alojamiento.'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 15_intervention_component.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Componentes intervenidos
-- Tabla: intervention_component
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO intervention_component (
    id, intervention_id, component_name, action, description
)
VALUES
    (
        '0f000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'Soporte del motor',
        'ADJUSTED',
        'Ajuste de fijaciones y control posterior de vibración.'
    ),
    (
        '0f000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000002',
        'Filtro de retorno',
        'REPLACED',
        'Filtro reemplazado durante el mantenimiento preventivo.'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 16_checklist_response.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Respuestas de checklist
-- Tabla: checklist_response
-- Dependencias: intervention, checklist_item
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO checklist_response (
    id, intervention_id, checklist_item_id, value, observation
)
VALUES
    (
        '10000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        '0b000000-0000-4000-8000-000000000001',
        'OK',
        'Nivel dentro del rango esperado.'
    ),
    (
        '10000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000001',
        '0b000000-0000-4000-8000-000000000002',
        'OK_AFTER_ADJUSTMENT',
        'La vibración disminuyó luego del ajuste de fijaciones.'
    ),
    (
        '10000000-0000-4000-8000-000000000003',
        '0c000000-0000-4000-8000-000000000002',
        '0b000000-0000-4000-8000-000000000003',
        'REPLACED',
        'Filtro saturado reemplazado.'
    ),
    (
        '10000000-0000-4000-8000-000000000004',
        '0c000000-0000-4000-8000-000000000002',
        '0b000000-0000-4000-8000-000000000004',
        'OK',
        NULL
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 17_technical_note.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Notas técnicas
-- Tabla: technical_note
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO technical_note (id, intervention_id, content)
VALUES
    (
        '11000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'Controlar nuevamente vibración y torque de fijaciones en la próxima visita.'
    ),
    (
        '11000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000002',
        'Revisar estado del nuevo filtro durante el próximo mantenimiento preventivo.'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 18_evidence.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Evidencias
-- Tabla: evidence
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO evidence (
    id, intervention_id, type, reference, description
)
VALUES
    (
        '12000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'PHOTO',
        'examples/evidence/ot-001/compresor-antes.jpg',
        'Estado del compresor antes del ajuste.'
    ),
    (
        '12000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000001',
        'MEASUREMENT',
        'examples/evidence/ot-001/medicion-vibracion.json',
        'Referencia de ejemplo a la medición posterior al ajuste.'
    ),
    (
        '12000000-0000-4000-8000-000000000003',
        '0c000000-0000-4000-8000-000000000002',
        'PHOTO',
        'examples/evidence/ot-004/filtro-reemplazado.jpg',
        'Filtro nuevo instalado en la unidad HVAC.'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 19_conformity.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Conformidades
-- Tabla: conformity
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO conformity (id, intervention_id, signature)
VALUES
    (
        '13000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'example-signature-reference/acme-industrial/ot-001'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 20_preventive_maintenance_plan.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Planes de mantenimiento preventivo
-- Tabla: preventive_maintenance_plan
-- Dependencias: equipment, service_type
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO preventive_maintenance_plan (
    id, equipment_id, service_type_id, next_execution_at
)
VALUES
    (
        '14000000-0000-4000-8000-000000000001',
        '04000000-0000-4000-8000-000000000001',
        '05000000-0000-4000-8000-000000000001',
        '2026-10-08T09:00:00-03:00'
    ),
    (
        '14000000-0000-4000-8000-000000000002',
        '04000000-0000-4000-8000-000000000004',
        '05000000-0000-4000-8000-000000000001',
        '2026-10-09T10:00:00-03:00'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- 21_recurrence.sql
-- ============================================================
-- FieldFlow - Seeder de ejemplo: Recurrencias
-- Tabla: recurrence
-- Dependencias: preventive_maintenance_plan
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.


INSERT INTO recurrence (
    id, preventive_maintenance_plan_id, frequency, "interval"
)
VALUES
    (
        '15000000-0000-4000-8000-000000000001',
        '14000000-0000-4000-8000-000000000001',
        'MONTH',
        1
    ),
    (
        '15000000-0000-4000-8000-000000000002',
        '14000000-0000-4000-8000-000000000002',
        'MONTH',
        3
    )
ON CONFLICT DO NOTHING;


COMMIT;
