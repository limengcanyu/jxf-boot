-- create schema if not exists flaw;

create table if not exists flaw.example_table
(
    id        int auto_increment primary key,
    user_id   varchar(20)    null,
    zh_name   varchar(20)    null,
    age       tinyint        null,
    address   varchar(100)   null
);

