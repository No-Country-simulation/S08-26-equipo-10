# FieldFlow — Seeders de ejemplo

Estos scripts cargan datos de ejemplo sobre el esquema existente de FieldFlow.

## Propósito

- No son migraciones de Flyway.
- No deben ubicarse en el directorio de migraciones versionadas.
- Están pensados para desarrollo, pruebas manuales y demostraciones.
- Asumen que `V1__initial_schema.sql` ya fue ejecutado.
- Usan UUID fijos para que las relaciones sean reproducibles.
- Todos los `INSERT` finalizan con `ON CONFLICT DO NOTHING`, por lo que volver a ejecutar el mismo conjunto no duplica los registros definidos por estos seeders.

## Orden de ejecución

Ejecutar los archivos en este orden:

1. `01_client.sql`
2. `02_site.sql`
3. `03_installation.sql`
4. `04_equipment.sql`
5. `05_service_type.sql`
6. `06_technician.sql`
7. `07_technician_availability.sql`
8. `08_work_order.sql`
9. `09_assignment.sql`
10. `10_checklist.sql`
11. `11_checklist_item.sql`
12. `12_intervention.sql`
13. `13_failure.sql`
14. `14_repair.sql`
15. `15_intervention_component.sql`
16. `16_checklist_response.sql`
17. `17_technical_note.sql`
18. `18_evidence.sql`
19. `19_conformity.sql`
20. `20_preventive_maintenance_plan.sql`
21. `21_recurrence.sql`

## Ejecución completa

`seed_all.sql` contiene el mismo conjunto de datos en el orden correcto y puede pegarse directamente en el SQL Editor de Supabase o ejecutarse mediante `psql`.

## Escenarios incluidos

Los datos permiten observar, entre otros:

- clientes con múltiples sedes;
- instalaciones opcionales;
- un equipo sin instalación;
- tipos de servicio y técnicos precargados;
- disponibilidades;
- OT en distintos estados (`PENDING`, `ASSIGNED`, `PENDING_CUSTOMER_CONFIRMATION`, `COMPLETED`);
- asignaciones sin solapamiento;
- una intervención completada con conformidad;
- una intervención pendiente de conformidad;
- fallas, reparaciones y componentes intervenidos;
- checklists y respuestas;
- notas técnicas y referencias de evidencia;
- historial derivable desde OT/intervenciones;
- planes preventivos con recurrencia simple.

## Importante

Los valores de `evidence.reference` y `conformity.signature` son referencias de ejemplo; estos seeders no crean archivos ni implementan almacenamiento físico.

Los seeders respetan las relaciones y restricciones SQL del esquema, además de las reglas funcionales principales documentadas para el MVP cuando es posible representarlas mediante datos.
