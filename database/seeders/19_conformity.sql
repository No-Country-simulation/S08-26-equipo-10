-- FieldFlow - Seeder de ejemplo: Conformidades
-- Tabla: conformity
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO conformity (id, intervention_id, signature)
VALUES
    (
        '13000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'example-signature-reference/acme-industrial/ot-001'
    )
ON CONFLICT DO NOTHING;

COMMIT;
