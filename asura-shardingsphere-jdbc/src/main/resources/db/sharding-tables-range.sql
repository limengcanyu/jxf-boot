drop schema if exists ds0;

create schema ds0;

-- t_order
create table ds0.t_order_create_time_2020_q1
(
    id          int auto_increment primary key,
    user_id     int not null,
    order_id    int not null,
    create_time datetime not null
);

create table ds0.t_order_create_time_2020_q2
(
    id          int auto_increment primary key,
    user_id     int not null,
    order_id    int not null,
    create_time datetime not null
);

-- t_order_item
create table ds0.t_order_item_create_time_2020_q1
(
    id            int auto_increment primary key,
    user_id       int not null,
    order_id      int not null,
    order_item_id int not null,
    create_time   datetime not null
);

create table ds0.t_order_item_create_time_2020_q2
(
    id            int auto_increment primary key,
    user_id       int not null,
    order_id      int not null,
    order_item_id int not null,
    create_time   datetime not null
);

