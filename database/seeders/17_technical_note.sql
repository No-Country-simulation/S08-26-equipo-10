-- FieldFlow - Seeder de ejemplo: Notas técnicas
-- Tabla: technical_note
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO technical_note (id, intervention_id, content)
VALUES
    (
        '11000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'Controlar nuevamente vibración y torque de fijaciones en la próxima visita.'
    ),
    (
        '11000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000002',
        'Revisar estado del nuevo filtro durante el próximo mantenimiento preventivo.'
    )
ON CONFLICT DO NOTHING;

COMMIT;
