alter table crews
    drop foreign key crews_events_id_fk;

drop index crews_event_id_number_uindex on crews;

alter table crews
    drop column number;

alter table crews
    add constraint crews_events_id_fk
        foreign key (event_id) references events (id)
            on delete cascade;

alter table qr_codes
    modify qr varchar(128) not null after crew_id;

alter table qr_codes
    add number int not null after event_id;

truncate table qr_codes;

create unique index qr_codes_event_id_number_uindex
    on qr_codes (event_id, number);

alter table events
    add max_crew_number int default 20 not null;

alter table qr_codes
    drop foreign key qr_codes_crews_id_fk;

alter table qr_codes
    drop column crew_id;

alter table crews
    add qr_code int null;

create unique index crews_qr_code_uindex
    on crews (qr_code);

alter table crews
    add constraint crews_qr_codes_id_fk
        foreign key (qr_code) references qr_codes (id)
            on delete set null;

