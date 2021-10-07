create table languages
(
    id   int auto_increment,
    code varchar(4) not null,
    constraint languages_pk
        primary key (id)
);

create
    unique index languages_code_uindex
    on languages (code);


create table event_language_codes
(
    id int auto_increment,
    constraint event_language_codes_pk
        primary key (id)
);

create table events
(
    id                  int auto_increment,
    name_id             int null,
    description_id      int null,
    start_date          datetime not null,
    end_date            datetime null,
    main_photo          varchar(128) null,
    photos              json null,
    default_language_id int      not null,
    qr_code_template    varchar(128) null,
    nr_template         varchar(128) null,
    constraint events_pk
        primary key (id),
    constraint events_event_language_codes_id_fk1
        foreign key (name_id) references event_language_codes (id)
            on delete cascade,
    constraint events_event_language_codes_id_fk2
        foreign key (description_id) references event_language_codes (id)
            on delete cascade,
    constraint events_languages_id_fk
        foreign key (default_language_id) references languages (id)
);


create table users
(
    id            int auto_increment,
    login         varchar(32)            not null,
    first_name    varchar(64)            not null,
    last_name     varchar(64)            not null,
    password      varchar(60)            not null,
    phone         varchar(16)            not null,
    email         varchar(64)            not null,
    accepted_reg  boolean                not null,
    accepted_rodo boolean                not null,
    create_time   datetime default now() not null,
    last_login    datetime null,
    constraint users_pk
        primary key (id)
);

create
    unique index users_login_uindex
    on users (login);


create table user_groups
(
    id             int auto_increment,
    event_id       int null,
    user_id        int not null,
    selected_group enum ('owner','judge','organizer','admin') not null,
    constraint user_groups_pk
        primary key (id),
    constraint user_groups_events_id_fk
        foreign key (event_id) references events (id)
            on delete cascade,
    constraint user_groups_users_id_fk
        foreign key (user_id) references users (id)
            on delete cascade
);

create
    unique index user_groups_event_id_user_id_uindex
    on user_groups (event_id, user_id);

create table event_schedule
(
    id             int auto_increment,
    event_id       int      not null,
    selected_order int      not null,
    start_time     datetime not null,
    time           int      not null,
    constraint event_schedule_pk
        primary key (id),
    constraint event_schedule_events_id_fk
        foreign key (event_id) references events (id)
            on delete cascade
);

create
    unique index event_schedule_event_id_selected_order_uindex
    on event_schedule (event_id, selected_order);


create table event_languages
(
    id          int auto_increment,
    event_id    int not null,
    language_id int not null,
    constraint event_languages_pk
        primary key (id),
    constraint event_languages_events_id_fk
        foreign key (event_id) references events (id)
            on delete cascade
);

create
    unique index event_languages_event_id_language_id_uindex
    on event_languages (event_id, language_id);


create table dictionaries
(
    id                int auto_increment,
    event_language_id int        not null,
    code_id           int        not null,
    value             mediumtext not null,
    constraint dictionaries_pk
        primary key (id),
    constraint dictionaries_event_language_codes_id_fk
        foreign key (code_id) references event_language_codes (id)
            on delete cascade,
    constraint dictionaries_event_languages_id_fk
        foreign key (event_language_id) references event_languages (id)
            on delete cascade
);

create
    unique index dictionaries_event_language_id_code_uindex
    on dictionaries (event_language_id, code_id);

create table categories
(
    id             int auto_increment,
    event_id       int not null,
    name_id        int null,
    description_id int null,
    mode           enum ('year', 'open', 'other') default 'open' not null,
    min_year       int null,
    max_year       int null,
    constraint categories_pk
        primary key (id),
    constraint categories_event_language_codes_id_fk
        foreign key (name_id) references event_language_codes (id)
            on delete cascade,
    constraint categories_event_language_codes_id_fk_2
        foreign key (description_id) references event_language_codes (id)
            on delete cascade,
    constraint categories_events_id_fk
        foreign key (event_id) references events (id)
);

create table crews
(
    id            int auto_increment,
    event_id      int         not null,
    number        int null,
    car           varchar(64) not null,
    photo         varchar(128) null,
    description   mediumtext null,
    year_of_production year not null,
    driver_name   varchar(64) null,
    phone         varchar(16) not null,
    accepted_reg  boolean     not null,
    accepted_rodo boolean     not null,
    constraint crews_pk
        primary key (id),
    constraint crews_events_id_fk
        foreign key (event_id) references events (id)
            on delete cascade
);

create
    unique index crews_event_id_number_uindex
    on crews (event_id, number);

create table crew_categories
(
    id             int auto_increment,
    crew_id        int not null,
    category_id    int not null,
    ranking_points float null,
    constraint crew_categories_pk
        primary key (id),
    constraint crew_categories_crews_id_fk
        foreign key (crew_id) references crews (id)
            on delete cascade,
    constraint crew_categories_categories_id_fk
        foreign key (category_id) references categories (id)
            on delete cascade
);

create
    index crew_categories_ranking_points_index
    on crew_categories (ranking_points);

create table qr_codes
(
    id       int auto_increment,
    qr       varchar(128) not null,
    event_id int          not null,
    crew_id  int null,
    constraint qr_codes_pk
        primary key (id),
    constraint qr_codes_crews_id_fk
        foreign key (crew_id) references crews (id)
            on delete set null,
    constraint qr_codes_events_id_fk
        foreign key (event_id) references events (id)
            on delete cascade
);

create
    unique index qr_codes_qr_uindex
    on qr_codes (qr);

create table competitions
(
    id                 int auto_increment,
    event_id           int     not null,
    name_id            int     not null,
    description_id     int     not null,
    type               enum ('REGULAR_DRIVE', 'TIMER', 'BEST_MIN', 'BEST_MAX', 'COUNTED') not null,
    absence_points     int     not null,
    time               int     not null,
    max_ranking_points int     not null,
    current_state      enum ('BEFORE', 'DURING', 'AFTER') default 'BEFORE' not null,
    column_8           int null,
    number_of_subsets  int null,
    might_be_invalid   boolean not null,
    additiona1         float null,
    additional2        float null,
    additional3        float null,
    constraint competitions_pk
        primary key (id),
    constraint competitions_events_id_fk
        foreign key (event_id) references events (id),
    constraint competitions_event_language_codes_id_fk1
        foreign key (name_id) references event_language_codes (id)
            on delete cascade,
    constraint competitions_event_language_codes_id_fk2
        foreign key (description_id) references event_language_codes (id)
            on delete cascade
);

create table competition_fields
(
    id             int auto_increment,
    competition_id int not null,
    label_id       int not null,
    type           enum ('FLOAT', 'INT', 'BOOLEAN') not null,
    constraint competition_fields_pk
        primary key (id),
    constraint competition_fields_competitions_id_fk
        foreign key (competition_id) references competitions (id),
    constraint competition_fields_event_language_codes_id_fk
        foreign key (label_id) references event_language_codes (id)
            on delete cascade
);

create table scores
(
    id             int auto_increment,
    competition_id int                   not null,
    crew_id        int                   not null,
    invalid_result boolean default false not null,
    error_occured  boolean default false not null,
    additional1    float null,
    additional2    float null,
    additional3    float null,
    additional4    float null,
    additional5    float null,
    result         float null,
    constraint scores_pk
        primary key (id),
    constraint scores_competitions_id_fk
        foreign key (competition_id) references competitions (id)
            on delete cascade,
    constraint scores_crews_id_fk
        foreign key (crew_id) references crews (id)
            on delete cascade
);

create
    index scores_result_index
    on scores (result);

