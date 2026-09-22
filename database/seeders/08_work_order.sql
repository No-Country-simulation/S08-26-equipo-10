-- FieldFlow - Seeder de ejemplo: Órdenes de trabajo
-- Tabla: work_order
-- Dependencias: equipment, service_type
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

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

COMMIT;
