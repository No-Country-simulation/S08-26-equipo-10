ALTER TABLE work_order
    ADD CONSTRAINT ck_work_order_priority
        CHECK (
            priority IN (
                         'LOW',
                         'MEDIUM',
                         'HIGH',
                         'CRITICAL'
                )
            );