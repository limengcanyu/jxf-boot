drop schema if exists ds0;

create schema ds0;

-- t_order
create table ds0.t_order_0
(
    id          int auto_increment primary key,
    user_id     varchar(20) not null,
    order_id    varchar(20) not null,
    create_time datetime not null
);

create table ds0.t_order_1
(
    id          int auto_increment primary key,
    user_id     varchar(20) not null,
    order_id    varchar(20) not null,
    create_time datetime not null
);

-- t_order_item
create table ds0.t_order_item_0
(
    id          int auto_increment primary key,
    user_id     varchar(20) not null,
    order_id    varchar(20) not null,
    order_item_id    varchar(20) not null,
    create_time datetime not null
);

create table ds0.t_order_item_1
(
    id          int auto_increment primary key,
    user_id     varchar(20) not null,
    order_id    varchar(20) not null,
    order_item_id    varchar(20) not null,
    create_time datetime not null
);

