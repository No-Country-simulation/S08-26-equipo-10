-- FieldFlow - Seeder de ejemplo: Técnicos
-- Tabla: technician
-- Dependencias: ninguna
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO technician (id, name)
VALUES
    ('06000000-0000-4000-8000-000000000001', 'Juan Pérez'),
    ('06000000-0000-4000-8000-000000000002', 'Lucía Gómez'),
    ('06000000-0000-4000-8000-000000000003', 'Martín Díaz')
ON CONFLICT DO NOTHING;

COMMIT;
