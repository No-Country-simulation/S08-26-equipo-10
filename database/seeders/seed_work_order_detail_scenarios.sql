-- FieldFlow - Seeder de escenarios para GET /work-orders/{workOrderId}
-- Objetivo:
--   Probar el endpoint de detalle con distintas combinaciones de relaciones opcionales,
--   colecciones vacías, campos NULL, intervenciones parciales y múltiples intervenciones.
--
-- Requisito:
--   Ejecutar DESPUÉS de seed_all.sql, ya que reutiliza equipment, service_type y technician.
--
-- No forma parte de Flyway.
-- Los UUID son fijos para facilitar pruebas reproducibles.
--
-- Conteo SQL esperado con el fetch plan diseñado:
--
--   SIN intervenciones:
--     1. WorkOrder + relaciones to-one
--     2. Checklist + items
--     3. Interventions + technician + conformity
--     TOTAL: 3 queries
--
--   CON al menos una intervención:
--     Las 3 anteriores +
--     4. Failure
--     5. Repair
--     6. InterventionComponent
--     7. ChecklistItemAnswer + ChecklistItem
--     8. TechnicalNote
--     9. Evidence
--     TOTAL: 9 queries
--
-- IMPORTANTE:
--   Esto supone que WorkOrder ya NO mantiene una relación inversa @OneToOne hacia Checklist
--   que provoque una consulta secundaria automática de Hibernate.
--
-- Escenarios:
--   WO-101: mínima; installation NULL, assignment NULL, checklist NULL, interventions []
--   WO-102: checklist presente pero items []; assignment NULL; interventions []
--   WO-103: una intervención con todos sus detalles vacíos y campos nullable en NULL
--   WO-104: una intervención parcialmente poblada; mezcla de listas vacías y datos NULL
--   WO-105: múltiples intervenciones con detalles repartidos; prueba principal contra N+1
--   WO-106: WorkOrder completamente poblada
--
BEGIN;

-- ============================================================
-- WORK ORDERS
-- ============================================================

INSERT INTO work_order (
    id,
    equipment_id,
    service_type_id,
    instructions,
    priority,
    estimated_duration,
    status
)
VALUES
    (
        '08000000-0000-4000-8000-000000000101',
        -- EQ-003 no posee installation.
        '04000000-0000-4000-8000-000000000003',
        '05000000-0000-4000-8000-000000000003',
        'Escenario de prueba: Work Order mínima sin relaciones operativas opcionales.',
        'LOW',
        30,
        'PENDING'
    ),
    (
        '08000000-0000-4000-8000-000000000102',
        '04000000-0000-4000-8000-000000000001',
        '05000000-0000-4000-8000-000000000001',
        'Escenario de prueba: checklist existente sin ítems.',
        'MEDIUM',
        45,
        'PENDING'
    ),
    (
        '08000000-0000-4000-8000-000000000103',
        '04000000-0000-4000-8000-000000000002',
        '05000000-0000-4000-8000-000000000002',
        'Escenario de prueba: intervención activa sin detalles asociados.',
        'HIGH',
        120,
        'IN_PROGRESS'
    ),
    (
        '08000000-0000-4000-8000-000000000104',
        '04000000-0000-4000-8000-000000000004',
        '05000000-0000-4000-8000-000000000001',
        'Escenario de prueba: intervención con información parcial.',
        'MEDIUM',
        90,
        'PENDING_CUSTOMER_CONFIRMATION'
    ),
    (
        '08000000-0000-4000-8000-000000000105',
        '04000000-0000-4000-8000-000000000001',
        '05000000-0000-4000-8000-000000000002',
        'Escenario de prueba: múltiples intervenciones para verificar consultas masivas.',
        'HIGH',
        180,
        'PENDING_CUSTOMER_CONFIRMATION'
    ),
    (
        '08000000-0000-4000-8000-000000000106',
        '04000000-0000-4000-8000-000000000004',
        '05000000-0000-4000-8000-000000000001',
        'Escenario de prueba: Work Order completamente poblada.',
        'CRITICAL',
        120,
        'COMPLETED'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- ASSIGNMENTS
-- WO-101: NULL
-- WO-102: NULL
-- WO-103..106: presentes
-- ============================================================

INSERT INTO assignment (
    id,
    work_order_id,
    technician_id,
    planned_start_at,
    planned_end_at
)
VALUES
    (
        '09000000-0000-4000-8000-000000000103',
        '08000000-0000-4000-8000-000000000103',
        '06000000-0000-4000-8000-000000000002',
        '2026-09-10T13:00:00-03:00',
        '2026-09-10T15:00:00-03:00'
    ),
    (
        '09000000-0000-4000-8000-000000000104',
        '08000000-0000-4000-8000-000000000104',
        '06000000-0000-4000-8000-000000000003',
        '2026-09-11T09:00:00-03:00',
        '2026-09-11T10:30:00-03:00'
    ),
    (
        '09000000-0000-4000-8000-000000000105',
        '08000000-0000-4000-8000-000000000105',
        '06000000-0000-4000-8000-000000000001',
        '2026-09-12T08:00:00-03:00',
        '2026-09-12T11:00:00-03:00'
    ),
    (
        '09000000-0000-4000-8000-000000000106',
        '08000000-0000-4000-8000-000000000106',
        '06000000-0000-4000-8000-000000000003',
        '2026-09-13T09:00:00-03:00',
        '2026-09-13T11:00:00-03:00'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- CHECKLISTS
-- WO-101: NULL
-- WO-102: existe, pero sin items
-- WO-103: NULL
-- WO-104..106: con items
-- ============================================================

INSERT INTO checklist (id, work_order_id, name)
VALUES
    (
        '0a000000-0000-4000-8000-000000000102',
        '08000000-0000-4000-8000-000000000102',
        'Checklist vacío - prueba'
    ),
    (
        '0a000000-0000-4000-8000-000000000104',
        '08000000-0000-4000-8000-000000000104',
        'Checklist parcial - prueba'
    ),
    (
        '0a000000-0000-4000-8000-000000000105',
        '08000000-0000-4000-8000-000000000105',
        'Checklist múltiples intervenciones - prueba'
    ),
    (
        '0a000000-0000-4000-8000-000000000106',
        '08000000-0000-4000-8000-000000000106',
        'Checklist completo - prueba'
    )
ON CONFLICT DO NOTHING;


INSERT INTO checklist_item (id, checklist_id, label)
VALUES
    (
        '0b000000-0000-4000-8000-000000000104',
        '0a000000-0000-4000-8000-000000000104',
        'Verificar estado general'
    ),
    (
        '0b000000-0000-4000-8000-000000000105',
        '0a000000-0000-4000-8000-000000000105',
        'Verificar presión'
    ),
    (
        '0b000000-0000-4000-8000-000000000115',
        '0a000000-0000-4000-8000-000000000105',
        'Verificar temperatura'
    ),
    (
        '0b000000-0000-4000-8000-000000000106',
        '0a000000-0000-4000-8000-000000000106',
        'Verificar filtros'
    ),
    (
        '0b000000-0000-4000-8000-000000000116',
        '0a000000-0000-4000-8000-000000000106',
        'Verificar conexiones eléctricas'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- INTERVENTIONS
-- WO-101: []
-- WO-102: []
-- WO-103: 1 intervención sin sublistas
-- WO-104: 1 intervención parcial
-- WO-105: 2 intervenciones
-- WO-106: 1 intervención completa
-- ============================================================

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
        '0c000000-0000-4000-8000-000000000103',
        '08000000-0000-4000-8000-000000000103',
        '06000000-0000-4000-8000-000000000002',
        '2026-09-10T13:05:00-03:00',
        NULL,
        'IN_PROGRESS',
        NULL,
        NULL
    ),
    (
        '0c000000-0000-4000-8000-000000000104',
        '08000000-0000-4000-8000-000000000104',
        '06000000-0000-4000-8000-000000000003',
        '2026-09-11T09:05:00-03:00',
        '2026-09-11T10:10:00-03:00',
        'PENDING_CUSTOMER_CONFIRMATION',
        'Trabajo parcialmente documentado.',
        NULL
    ),
    (
        '0c000000-0000-4000-8000-000000000105',
        '08000000-0000-4000-8000-000000000105',
        '06000000-0000-4000-8000-000000000001',
        '2026-09-12T08:05:00-03:00',
        '2026-09-12T09:00:00-03:00',
        'COMPLETED',
        'Primera visita finalizada.',
        'Se requiere una segunda visita de verificación.'
    ),
    (
        '0c000000-0000-4000-8000-000000000115',
        '08000000-0000-4000-8000-000000000105',
        '06000000-0000-4000-8000-000000000001',
        '2026-09-12T10:00:00-03:00',
        '2026-09-12T10:45:00-03:00',
        'PENDING_CUSTOMER_CONFIRMATION',
        'Segunda visita completada.',
        NULL
    ),
    (
        '0c000000-0000-4000-8000-000000000106',
        '08000000-0000-4000-8000-000000000106',
        '06000000-0000-4000-8000-000000000003',
        '2026-09-13T09:05:00-03:00',
        '2026-09-13T10:50:00-03:00',
        'COMPLETED',
        'Mantenimiento completo ejecutado correctamente.',
        'Sin novedades pendientes.'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- FAILURES
-- WO-103: []
-- WO-104: 1
-- WO-105: solo en primera intervención
-- WO-106: 1
-- ============================================================

INSERT INTO failure (id, intervention_id, description)
VALUES
    (
        '0d000000-0000-4000-8000-000000000104',
        '0c000000-0000-4000-8000-000000000104',
        'Se detectó desgaste leve.'
    ),
    (
        '0d000000-0000-4000-8000-000000000105',
        '0c000000-0000-4000-8000-000000000105',
        'Presión fuera del rango esperado durante la primera visita.'
    ),
    (
        '0d000000-0000-4000-8000-000000000106',
        '0c000000-0000-4000-8000-000000000106',
        'Filtro con saturación.'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- REPAIRS
-- WO-103: []
-- WO-104: []
-- WO-105: solo en primera intervención
-- WO-106: 1
-- ============================================================

INSERT INTO repair (id, intervention_id, description)
VALUES
    (
        '0e000000-0000-4000-8000-000000000105',
        '0c000000-0000-4000-8000-000000000105',
        'Se corrigió el ajuste responsable de la pérdida de presión.'
    ),
    (
        '0e000000-0000-4000-8000-000000000106',
        '0c000000-0000-4000-8000-000000000106',
        'Se reemplazó el filtro saturado.'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- INTERVENTION COMPONENTS
-- Algunos description son NULL intencionalmente.
-- ============================================================

INSERT INTO intervention_component (
    id,
    intervention_id,
    component_name,
    action,
    description
)
VALUES
    (
        '0f000000-0000-4000-8000-000000000104',
        '0c000000-0000-4000-8000-000000000104',
        'Cubierta protectora',
        'INSPECTED',
        NULL
    ),
    (
        '0f000000-0000-4000-8000-000000000115',
        '0c000000-0000-4000-8000-000000000115',
        'Válvula de regulación',
        'ADJUSTED',
        'Ajuste final realizado durante la segunda visita.'
    ),
    (
        '0f000000-0000-4000-8000-000000000106',
        '0c000000-0000-4000-8000-000000000106',
        'Filtro principal',
        'REPLACED',
        'Filtro reemplazado y alojamiento limpiado.'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- CHECKLIST RESPONSES
-- Incluye observation NULL.
-- ============================================================

INSERT INTO checklist_response (
    id,
    intervention_id,
    checklist_item_id,
    value,
    observation
)
VALUES
    (
        '10000000-0000-4000-8000-000000000104',
        '0c000000-0000-4000-8000-000000000104',
        '0b000000-0000-4000-8000-000000000104',
        'OK',
        NULL
    ),
    (
        '10000000-0000-4000-8000-000000000105',
        '0c000000-0000-4000-8000-000000000105',
        '0b000000-0000-4000-8000-000000000105',
        'OUT_OF_RANGE',
        'Se detectó presión fuera de rango.'
    ),
    (
        '10000000-0000-4000-8000-000000000115',
        '0c000000-0000-4000-8000-000000000115',
        '0b000000-0000-4000-8000-000000000115',
        'OK',
        NULL
    ),
    (
        '10000000-0000-4000-8000-000000000106',
        '0c000000-0000-4000-8000-000000000106',
        '0b000000-0000-4000-8000-000000000106',
        'REPLACED',
        'Filtro reemplazado.'
    ),
    (
        '10000000-0000-4000-8000-000000000116',
        '0c000000-0000-4000-8000-000000000106',
        '0b000000-0000-4000-8000-000000000116',
        'OK',
        NULL
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- TECHNICAL NOTES
-- WO-103: []
-- WO-104: []
-- WO-105: una nota en primera intervención
-- WO-106: 1
-- ============================================================

INSERT INTO technical_note (id, intervention_id, content)
VALUES
    (
        '11000000-0000-4000-8000-000000000105',
        '0c000000-0000-4000-8000-000000000105',
        'Verificar estabilidad de presión durante la segunda visita.'
    ),
    (
        '11000000-0000-4000-8000-000000000106',
        '0c000000-0000-4000-8000-000000000106',
        'Revisar el filtro nuevamente en el próximo mantenimiento.'
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- EVIDENCE
-- Incluye description NULL.
-- ============================================================

INSERT INTO evidence (
    id,
    intervention_id,
    type,
    reference,
    description
)
VALUES
    (
        '12000000-0000-4000-8000-000000000104',
        '0c000000-0000-4000-8000-000000000104',
        'PHOTO',
        'examples/evidence/test/wo-104.jpg',
        NULL
    ),
    (
        '12000000-0000-4000-8000-000000000115',
        '0c000000-0000-4000-8000-000000000115',
        'MEASUREMENT',
        'examples/evidence/test/wo-105-second-visit.json',
        'Medición posterior al ajuste final.'
    ),
    (
        '12000000-0000-4000-8000-000000000106',
        '0c000000-0000-4000-8000-000000000106',
        'PHOTO',
        'examples/evidence/test/wo-106-after.jpg',
        'Estado final del equipo.'
    ),
    (
        '12000000-0000-4000-8000-000000000116',
        '0c000000-0000-4000-8000-000000000106',
        'MEASUREMENT',
        'examples/evidence/test/wo-106-measurement.json',
        NULL
    )
ON CONFLICT DO NOTHING;


-- ============================================================
-- CONFORMITY
-- Solo WO-106 posee conformidad.
-- WO-103, WO-104 y ambas intervenciones de WO-105 devuelven conformity = null.
-- ============================================================

INSERT INTO conformity (id, intervention_id, signature)
VALUES
    (
        '13000000-0000-4000-8000-000000000106',
        '0c000000-0000-4000-8000-000000000106',
        'example-signature-reference/test/wo-106'
    )
ON CONFLICT DO NOTHING;

COMMIT;
