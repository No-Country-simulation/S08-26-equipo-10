-- FieldFlow - Seeder de ejemplo: Evidencias
-- Tabla: evidence
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO evidence (
    id, intervention_id, type, reference, description
)
VALUES
    (
        '12000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'PHOTO',
        'examples/evidence/ot-001/compresor-antes.jpg',
        'Estado del compresor antes del ajuste.'
    ),
    (
        '12000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000001',
        'MEASUREMENT',
        'examples/evidence/ot-001/medicion-vibracion.json',
        'Referencia de ejemplo a la medición posterior al ajuste.'
    ),
    (
        '12000000-0000-4000-8000-000000000003',
        '0c000000-0000-4000-8000-000000000002',
        'PHOTO',
        'examples/evidence/ot-004/filtro-reemplazado.jpg',
        'Filtro nuevo instalado en la unidad HVAC.'
    )
ON CONFLICT DO NOTHING;

COMMIT;
