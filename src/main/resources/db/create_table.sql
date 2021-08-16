//script query create table
create table mountain
(
    id            varchar(255) not null,
    mountain_name varchar(30),
    photo         varchar(50),
    description   varchar(255),
    height        int,
    full_address  varchar(255),
    created_date  TIMESTAMP,
    created_by    varchar(30),
    updated_date  TIMESTAMP,
    updated_by    varchar(30),
    primary key (id)
);

create table bank
(
    id             varchar(255) not null,
    mountain_id    varchar(255),
    bank_name      varchar(255) not null,
    account_name   varchar(255) not null,
    account_number varchar(255) not null,
    is_deleted     int null,
    created_date   varchar(255),
    created_by     varchar(255),
    updated_date   varchar(255),
    updated_by     varchar(255),
    primary key (id),
    foreign key (mountain_id) references mountain (id)
);

create table basecamp
(
    id              varchar(255) not null,
    mountain_id     varchar(255) not null,
    basecamp_name   varchar(255) not null,
    description     varchar(255) not null,
    photo           varchar(255) not null,
    regulation      varchar(255) not null,
    full_address    varchar(255) not null,
    price           int          not null,
    total_climber   int,
    request_climber TIMESTAMP,
    created_date    TIMESTAMP,
    created_by      varchar(30),
    updated_date    TIMESTAMP,
    updated_by      varchar(30),
    primary key (id),
    foreign key (mountain_id) references mountain (id)
);

create table tracking
(
    id           varchar(255) not null,
    tracking_map varchar(30)  not null,
    status       varchar(30)  not null,
    reason       varchar(255) not null,
    created_date timestamp,
    created_by   varchar(30),
    updated_date timestamp,
    updated_by   varchar(30),
    primary key (id)
);

CREATE table USERS
(
    id               varchar(255)       not null,
    mountain_id      varchar(255),
    nik              varchar(16)        not null,
    phone_number     varchar(12) unique not null,
    username         varchar(20) unique not null,
    photo            varchar(50),
    first_name       varchar(30)        not null,
    last_name        varchar(30)        not null,
    address          varchar(255)       not null,
    email            varchar(30)        not null,
    pin              varchar(255)       not null,
    confirmation_pin varchar(255)       not null,
    role             varchar(20)        not null,
    is_deleted       int,
    created_date     timestamp,
    updated_date     timestamp,
    primary key (id),
    FOREIGN key (mountain_id) REFERENCES mountain (id)
);

CREATE table status
(
    id           varchar(255) not null,
    users_id     varchar(255) not null,
    mountain_id  varchar(255),
    username     varchar(30)  not null,
    role         varchar(20)  not null,
    status       varchar(300) not null,
    photo        varchar(50),
    created_date timestamp,
    primary key (id),
    foreign key (users_id) references USERS (id),
    foreign key (mountain_id) references mountain (id)
);

create table reply_status
(
    id           varchar(255) not null,
    status_id    varchar(300) not null,
    username     varchar(30)  not null,
    role         varchar(20)  not null,
    reply        varchar(300) not null,
    created_date timestamp,
    primary key (id),
    foreign key (status_id) references status (id)
);

CREATE table app
(
    id      int primary key,
    name    varchar(10)  not null,
    app_id  varchar(255) not null,
    app_key varchar(20)  not null
);

create table equipment
(
    id           varchar(255) primary key,
    back_pack    int not null,
    water        int not null,
    mattress     int not null,
    tent         int not null,
    food         int not null,
    stove        int not null,
    nesting      int not null,
    rain_coat    int not null,
    flash_light  int not null,
    created_date TIMESTAMP,
    updated_date TIMESTAMP
);

create table request_mountain
(
    id                varchar(255) not null,
    users_id          varchar(255) not null,
    basecamp_id       varchar(255) not null,
    equipment_id      varchar(255) not null,
    tracking_id       varchar(255) not null,
    total_climber     int          not null,
    mountain_name     varchar(30)  not null,
    tracking_status   int          not null,
    price             int          not null,
    payment           int,
    account_name      varchar(255) not null,
    bank_name         varchar(255) not null,
    photo_payment     varchar(255) not null,
    name_payment_user varchar(255),
    bank_payment_user varchar(255),
    climber_date      TIMESTAMP    not null,
    created_date      TIMESTAMP,
    updated_date      TIMESTAMP,
    primary key (id),
    FOREIGN key (users_id) REFERENCES users (id),
    FOREIGN key (basecamp_id) REFERENCES basecamp (id),
    FOREIGN key (equipment_id) REFERENCES equipment (id),
    foreign key (tracking_id) references tracking (id)
);

create table member
(
    id           varchar(255) not null,
    request_id   varchar(255) not null,
    nik          varchar(20)  not null,
    phone_number varchar(12)  not null,
    name         varchar(30)  not null,
    province     varchar(50)  not null,
    head         int          not null,
    city         varchar(50)  not null,
    sub_district varchar(50)  not null,
    village      varchar(50)  not null,
    full_address varchar(255) not null,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    primary key (id),
    FOREIGN KEY (request_id) references request_mountain (id)
);

insert into roles(name)
values ('USER');
insert into roles(name)
values ('RANGER');
insert into roles(name)
values ('SYSADMIN');