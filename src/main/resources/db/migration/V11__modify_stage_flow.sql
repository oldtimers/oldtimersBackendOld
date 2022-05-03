alter table events
    modify stage enum ('NEW', 'NUMBERS', 'PRESENTS', 'RESULTS') default 'NEW' not null;

