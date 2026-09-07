-- FieldFlow - Seeder de ejemplo: Equipos
-- Tabla: equipment
-- Dependencias: site, installation
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO equipment (
    id, site_id, installation_id, identifier, name, current_status
)
VALUES
    (
        '04000000-0000-4000-8000-000000000001',
        '02000000-0000-4000-8000-000000000001',
        '03000000-0000-4000-8000-000000000001',
        'EQ-001',
        'Compresor principal',
        'OPERATIONAL'
    ),
    (
        '04000000-0000-4000-8000-000000000002',
        '02000000-0000-4000-8000-000000000001',
        '03000000-0000-4000-8000-000000000002',
        'EQ-002',
        'Bomba centrífuga',
        'REQUIRES_ATTENTION'
    ),
    (
        '04000000-0000-4000-8000-000000000003',
        '02000000-0000-4000-8000-000000000002',
        NULL,
        'EQ-003',
        'Generador de respaldo',
        'OPERATIONAL'
    ),
    (
        '04000000-0000-4000-8000-000000000004',
        '02000000-0000-4000-8000-000000000003',
        '03000000-0000-4000-8000-000000000003',
        'EQ-004',
        'Unidad HVAC',
        'OPERATIONAL'
    )
ON CONFLICT DO NOTHING;

COMMIT;
