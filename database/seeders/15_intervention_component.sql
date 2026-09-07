-- FieldFlow - Seeder de ejemplo: Componentes intervenidos
-- Tabla: intervention_component
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO intervention_component (
    id, intervention_id, component_name, action, description
)
VALUES
    (
        '0f000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'Soporte del motor',
        'ADJUSTED',
        'Ajuste de fijaciones y control posterior de vibración.'
    ),
    (
        '0f000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000002',
        'Filtro de retorno',
        'REPLACED',
        'Filtro reemplazado durante el mantenimiento preventivo.'
    )
ON CONFLICT DO NOTHING;

COMMIT;
