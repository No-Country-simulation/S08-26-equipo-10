-- Descripción:
-- Crea la tabla evidence_upload para registrar cada archivo destinado a una intervención
-- antes de incorporarlo al reporte.
-- Guarda la referencia del objeto en Supabase y las fechas de creación, subida y asociación con evidence.
-- Esto permite identificar los archivos cuya subida terminó, pero que nunca se incluyeron en un reporte,
-- y eliminarlos del bucket después de un plazo de espera.
-- El intervention_id de esta tabla indica para qué intervención se subió el archivo;
-- attached_at indica si ya quedó asociado al reporte.

CREATE TABLE evidence_upload
(
    id              UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    intervention_id UUID        NOT NULL REFERENCES intervention (id),
    reference       TEXT        NOT NULL UNIQUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    uploaded_at     TIMESTAMPTZ NULL,
    attached_at     TIMESTAMPTZ NULL,
    evidence_id     UUID UNIQUE NULL REFERENCES evidence(id),

    CONSTRAINT ck_evidence_upload_attached
        CHECK (
            attached_at IS NULL
                OR (uploaded_at IS NOT NULL AND evidence_id IS NOT NULL)
            )
);