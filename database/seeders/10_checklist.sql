-- FieldFlow - Seeder de ejemplo: Checklists
-- Tabla: checklist
-- Dependencias: work_order
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

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

COMMIT;
