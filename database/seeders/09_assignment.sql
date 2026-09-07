-- FieldFlow - Seeder de ejemplo: Asignaciones
-- Tabla: assignment
-- Dependencias: work_order, technician, technician_availability
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

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

COMMIT;
