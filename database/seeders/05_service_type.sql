-- FieldFlow - Seeder de ejemplo: Tipos de servicio
-- Tabla: service_type
-- Dependencias: ninguna
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO service_type (id, name)
VALUES
    ('05000000-0000-4000-8000-000000000001', 'Mantenimiento preventivo'),
    ('05000000-0000-4000-8000-000000000002', 'Reparación correctiva'),
    ('05000000-0000-4000-8000-000000000003', 'Inspección técnica')
ON CONFLICT DO NOTHING;

COMMIT;
