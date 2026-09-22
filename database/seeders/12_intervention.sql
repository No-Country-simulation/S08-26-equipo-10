-- FieldFlow - Seeder de ejemplo: Intervenciones
-- Tabla: intervention
-- Dependencias: work_order, technician, assignment
-- Uso: ejecutar directamente sobre PostgreSQL/Supabase una vez creado el esquema.
-- No forma parte de las migraciones de Flyway.
-- Los UUID son fijos para mantener relaciones reproducibles entre seeders.

BEGIN;

INSERT INTO intervention (
    id,
    work_order_id,
    technician_id,
    started_at,
    ended_at,
    status,
    result,
    observations
)
VALUES
    (
        '0c000000-0000-4000-8000-000000000001',
        '08000000-0000-4000-8000-000000000001',
        '06000000-0000-4000-8000-000000000001',
        '2026-09-08T09:05:00-03:00',
        '2026-09-08T10:20:00-03:00',
        'COMPLETED',
        'Equipo operativo luego del mantenimiento.',
        'Se recomienda repetir el control de vibraciones en el próximo mantenimiento.'
    ),
    (
        '0c000000-0000-4000-8000-000000000002',
        '08000000-0000-4000-8000-000000000004',
        '06000000-0000-4000-8000-000000000003',
        '2026-09-09T10:05:00-03:00',
        '2026-09-09T11:10:00-03:00',
        'PENDING_CUSTOMER_CONFIRMATION',
        'Mantenimiento realizado correctamente.',
        'Se reemplazó un filtro saturado. Pendiente de conformidad del cliente.'
    )
ON CONFLICT DO NOTHING;

COMMIT;
