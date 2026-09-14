CREATE INDEX idx_failure_intervention_id
    ON failure (intervention_id);

CREATE INDEX idx_repair_intervention_id
    ON repair (intervention_id);

CREATE INDEX idx_intervention_component_intervention_id
    ON intervention_component (intervention_id);

CREATE INDEX idx_technical_note_intervention_id
    ON technical_note (intervention_id);

CREATE INDEX idx_evidence_intervention_id
    ON evidence (intervention_id);

CREATE INDEX idx_checklist_item_checklist_id
    ON checklist_item (checklist_id);