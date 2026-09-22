-- FieldFlow - Seeder de ejemplo: Clientes
-- Tabla: client
-- Dependencias: ninguna
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO client (id, name)
VALUES
    ('01000000-0000-4000-8000-000000000001', 'ACME Industrial'),
    ('01000000-0000-4000-8000-000000000002', 'Servicios Andinos')
ON CONFLICT DO NOTHING;

COMMIT;
