-- FieldFlow - Seeder de ejemplo: Disponibilidad de técnicos
-- Tabla: technician_availability
-- Dependencias: technician
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

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

COMMIT;
