alter table events
    drop column qr_code_template;

alter table events
    add stage enum ('NEW', 'NUMBERS', 'RESULTS') default 'NEW' not null;

alter table events
    drop column nr_template;

