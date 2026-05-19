DROP TABLE IF EXISTS artanis.sys_user;

CREATE TABLE artanis.sys_user
(
    id    BIGINT(20)  NOT NULL COMMENT '主键ID',
    name  VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age   INT(11)     NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS artanis.data_change_log;

CREATE TABLE artanis.data_change_log
(
    id               BIGINT(20) AUTO_INCREMENT COMMENT '主键ID',
    operation_result TEXT        NULL DEFAULT NULL COMMENT '操作结果',
    operator_id      varchar(20) NULL DEFAULT NULL COMMENT '操作人ID',
    operator_name    varchar(20) NULL DEFAULT NULL COMMENT '操作人姓名',
    create_time      datetime    NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id)
);
