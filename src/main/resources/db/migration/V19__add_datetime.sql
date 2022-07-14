alter table competition_fields
    modify type enum ('FLOAT', 'INT', 'BOOLEAN', 'TIMER', 'DATETIME') not null;