alter table competition_fields
    add order_info int not null;

create unique index competition_fields_competition_id_label_id_uindex
    on competition_fields (competition_id, label_id);

alter table competitions
    add function_code varchar(300) default '0' null;

alter table competition_fields
    modify type enum ('FLOAT', 'INT', 'BOOLEAN', 'TIMER') not null;

