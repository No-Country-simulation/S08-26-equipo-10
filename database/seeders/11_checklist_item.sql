-- FieldFlow - Seeder de ejemplo: Ítems de checklist
-- Tabla: checklist_item
-- Dependencias: checklist
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO checklist_item (id, checklist_id, label)
VALUES
    (
        '0b000000-0000-4000-8000-000000000001',
        '0a000000-0000-4000-8000-000000000001',
        'Verificar nivel de aceite'
    ),
    (
        '0b000000-0000-4000-8000-000000000002',
        '0a000000-0000-4000-8000-000000000001',
        'Verificar temperatura y vibraciones'
    ),
    (
        '0b000000-0000-4000-8000-000000000003',
        '0a000000-0000-4000-8000-000000000002',
        'Verificar estado de filtros'
    ),
    (
        '0b000000-0000-4000-8000-000000000004',
        '0a000000-0000-4000-8000-000000000002',
        'Verificar conexiones eléctricas'
    )
ON CONFLICT DO NOTHING;

COMMIT;
