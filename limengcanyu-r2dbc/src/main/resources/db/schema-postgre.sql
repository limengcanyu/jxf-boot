DROP TABLE IF EXISTS sys_user;

create table sys_user
(
    id int not null constraint user_pk primary key, -- 主键ID
    name varchar(30), -- 姓名
    age int, -- 年龄
    email varchar(50) -- 邮箱
);
