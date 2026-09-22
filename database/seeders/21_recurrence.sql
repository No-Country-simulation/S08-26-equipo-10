-- FieldFlow - Seeder de ejemplo: Recurrencias
-- Tabla: recurrence
-- Dependencias: preventive_maintenance_plan
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

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
