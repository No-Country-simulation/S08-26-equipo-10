-- FieldFlow - Seeder de ejemplo: Respuestas de checklist
-- Tabla: checklist_response
-- Dependencias: intervention, checklist_item
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO checklist_response (
    id, intervention_id, checklist_item_id, value, observation
)
VALUES
    (
        '10000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        '0b000000-0000-4000-8000-000000000001',
        'OK',
        'Nivel dentro del rango esperado.'
    ),
    (
        '10000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000001',
        '0b000000-0000-4000-8000-000000000002',
        'OK_AFTER_ADJUSTMENT',
        'La vibración disminuyó luego del ajuste de fijaciones.'
    ),
    (
        '10000000-0000-4000-8000-000000000003',
        '0c000000-0000-4000-8000-000000000002',
        '0b000000-0000-4000-8000-000000000003',
        'REPLACED',
        'Filtro saturado reemplazado.'
    ),
    (
        '10000000-0000-4000-8000-000000000004',
        '0c000000-0000-4000-8000-000000000002',
        '0b000000-0000-4000-8000-000000000004',
        'OK',
        NULL
    )
ON CONFLICT DO NOTHING;

COMMIT;
