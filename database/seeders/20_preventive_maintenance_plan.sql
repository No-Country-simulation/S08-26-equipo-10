-- FieldFlow - Seeder de ejemplo: Planes de mantenimiento preventivo
-- Tabla: preventive_maintenance_plan
-- Dependencias: equipment, service_type
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

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

COMMIT;
