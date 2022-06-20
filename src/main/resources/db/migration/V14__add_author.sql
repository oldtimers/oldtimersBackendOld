alter table scores
    add author_id int null;

alter table scores
    add constraint scores_users_id_fk
        foreign key (author_id) references users (id)
            on delete set null;

