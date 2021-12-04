create table refresh_token
(
    id          int auto_increment,
    expiry_date datetime     not null,
    token       varchar(255) not null,
    user_id     int          not null,
    constraint refresh_token_pk
        primary key (id),
    constraint refresh_token_users_id_fk
        foreign key (user_id) references users (id)
            on delete cascade
);

create unique index refresh_token_token_uindex
    on refresh_token (token);

