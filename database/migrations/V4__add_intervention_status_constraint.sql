-- V4__add_intervention_status_constraint.sql
ALTER TABLE intervention
    ADD CONSTRAINT ck_intervention_status
        CHECK (status IN (
                          'IN_PROGRESS',
                          'PENDING_CUSTOMER_CONFIRMATION',
                          'COMPLETED'
            ));