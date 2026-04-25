CREATE TABLE `write-ds`.`t_user`
(
    `user_id`     bigint(20) NOT NULL COMMENT '用户ID',
    `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
    `version`     int DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `read-ds-0`.`t_user` LIKE `write-ds`.`t_user`;

CREATE TABLE `read-ds-1`.`t_user` LIKE `write-ds`.`t_user`;

--

INSERT INTO `write-ds`.t_user (user_id, create_time, version) VALUES (1, '2022-06-16 14:38:39', 0);

INSERT INTO `read-ds-0`.t_user (user_id, create_time, version) VALUES (1, '2022-05-16 14:38:58', 0);

INSERT INTO `read-ds-1`.t_user (user_id, create_time, version) VALUES (1, '2022-04-16 14:39:16', 0);

--

drop schema if exists `write-ds`;
drop schema if exists `read-ds-0`;
drop schema if exists `read-ds-1`;
