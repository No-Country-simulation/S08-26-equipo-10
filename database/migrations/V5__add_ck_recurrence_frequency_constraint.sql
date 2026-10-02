ALTER TABLE recurrence
    ADD CONSTRAINT ck_recurrence_frequency
        CHECK (frequency IN ('DAY', 'WEEK', 'MONTH', 'YEAR'));