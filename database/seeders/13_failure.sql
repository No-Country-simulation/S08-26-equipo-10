-- FieldFlow - Seeder de ejemplo: Fallas
-- Tabla: failure
-- Dependencias: intervention
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO failure (id, intervention_id, description)
VALUES
    (
        '0d000000-0000-4000-8000-000000000001',
        '0c000000-0000-4000-8000-000000000001',
        'Se detectó vibración leve por fijaciones con ajuste insuficiente.'
    ),
    (
        '0d000000-0000-4000-8000-000000000002',
        '0c000000-0000-4000-8000-000000000002',
        'Filtro de retorno con saturación superior a la esperada.'
    )
ON CONFLICT DO NOTHING;

COMMIT;
