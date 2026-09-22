-- FieldFlow - Seeder de ejemplo: Reparaciones
-- Tabla: repair
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO repair (id, intervention_id, description)
VALUES
    (
        '0e000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'Se ajustaron las fijaciones del soporte del motor.'
    ),
    (
        '0e000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000002',
        'Se reemplazó el filtro de retorno y se limpió el alojamiento.'
    )
ON CONFLICT DO NOTHING;

COMMIT;
