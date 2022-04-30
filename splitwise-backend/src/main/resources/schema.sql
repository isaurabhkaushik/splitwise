
create table if not exists users(
    id serial primary key,
    is_active boolean not null,
    is_deleted boolean not null default false,
    create_at timestamptz not null default now(),
    update_at timestamptz not null default now(),
    phone varchar(10) unique not null,
    email varchar(50) unique not null,
    name varchar(20) not null,
    username varchar(20) not null,
    password varchar(100) unique not null,
    profile_url varchar(100) unique not null
);

create table if not exists groups(
    id serial primary key,
    is_deleted boolean not null default false,
    create_at timestamptz not null default now(),
    update_at timestamptz not null default now(),
    name varchar(100) not null,
    total_amount int not null default 0,
    txns text
);

create table if not exists expenses(
    id serial primary key,
    is_deleted boolean not null default false,
    create_at timestamptz not null default now(),
    update_at timestamptz not null default now(),
    group_id int references groups on delete cascade on update cascade,
    total_amount int not null default 0,
    shares text,
    receipt_url varchar(100) not null,
    created_by int references users on delete cascade on update cascade
);

create table if not exists group_share(
    id serial primary key,
    is_deleted boolean not null default false,
    create_at timestamptz not null default now(),
    update_at timestamptz not null default now(),
    user_id int references users on delete cascade on update cascade,
    group_id int references groups on delete cascade on update cascade,
    is_settled boolean not null default false,
    total_paid int default 0,
    total_owed int default 0
);


