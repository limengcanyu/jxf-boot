# 分库
drop schema ds0;
drop schema ds1;

create schema ds0;
create schema ds1;

-- t_order
create table ds0.t_order
(
    id          int auto_increment primary key,
    user_id     varchar(20) not null,
    order_id    varchar(20) not null,
    create_time datetime not null
);

create table ds1.t_order
(
    id          int auto_increment primary key,
    user_id     varchar(20) not null,
    order_id    varchar(20) not null,
    create_time datetime not null
);

-- t_order_item
create table ds0.t_order_item
(
    id          int auto_increment primary key,
    user_id     varchar(20) not null,
    order_id    varchar(20) not null,
    order_item_id    varchar(20) not null,
    create_time datetime not null
);

create table ds1.t_order_item
(
    id          int auto_increment primary key,
    user_id     varchar(20) not null,
    order_id    varchar(20) not null,
    order_item_id    varchar(20) not null,
    create_time datetime not null
);
