-- FieldFlow - Seeder de ejemplo: Sedes
-- Tabla: site
-- Dependencias: client
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO site (id, client_id, name, address)
VALUES
    (
        '02000000-0000-4000-8000-000000000001',
        '01000000-0000-4000-8000-000000000001',
        'Planta Córdoba',
        'Av. Circunvalación 4500, Córdoba, Argentina'
    ),
    (
        '02000000-0000-4000-8000-000000000002',
        '01000000-0000-4000-8000-000000000001',
        'Depósito Villa María',
        'Ruta Nacional 9 Km 555, Villa María, Córdoba, Argentina'
    ),
    (
        '02000000-0000-4000-8000-000000000003',
        '01000000-0000-4000-8000-000000000002',
        'Centro Operativo Mendoza',
        'Acceso Sur 2100, Godoy Cruz, Mendoza, Argentina'
    )
ON CONFLICT DO NOTHING;

COMMIT;
