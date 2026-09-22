-- FieldFlow - Seeder de ejemplo: Instalaciones
-- Tabla: installation
-- Dependencias: site
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO installation (id, site_id, name)
VALUES
    (
        '03000000-0000-4000-8000-000000000001',
        '02000000-0000-4000-8000-000000000001',
        'Sala de máquinas'
    ),
    (
        '03000000-0000-4000-8000-000000000002',
        '02000000-0000-4000-8000-000000000001',
        'Línea de producción A'
    ),
    (
        '03000000-0000-4000-8000-000000000003',
        '02000000-0000-4000-8000-000000000003',
        'Taller de mantenimiento'
    )
ON CONFLICT DO NOTHING;

COMMIT;
