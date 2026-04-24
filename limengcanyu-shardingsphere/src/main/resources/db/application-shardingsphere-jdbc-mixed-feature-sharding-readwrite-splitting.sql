drop schema if exists `ds`;
drop schema if exists `write-ds-0`;
drop schema if exists `write-ds-0-read-0`;
drop schema if exists `write-ds-0-read-1`;
drop schema if exists `write-ds-1`;
drop schema if exists `write-ds-1-read-0`;
drop schema if exists `write-ds-1-read-1`;
create schema `ds`;
create schema `write-ds-0`;
create schema `write-ds-0-read-0`;
create schema `write-ds-0-read-1`;
create schema `write-ds-1`;
create schema `write-ds-1-read-0`;
create schema `write-ds-1-read-1`;

--

CREATE TABLE `ds`.`t_order`
(
    `order_id`    bigint(20) NOT NULL COMMENT '订单ID',
    `user_id`     bigint(20) NOT NULL COMMENT '用户ID',
    `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
    `version`     int DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ds`.`t_order_item`
(
    `order_item_id` bigint(20) NOT NULL COMMENT '订单项ID',
    `order_id`      bigint(20) NOT NULL COMMENT '订单ID',
    `user_id`       bigint(20) NOT NULL COMMENT '用户ID',
    `create_time`   TIMESTAMP NOT NULL COMMENT '创建时间',
    `version`       int DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `write-ds-0`.`t_order_0` LIKE `ds`.`t_order`;
CREATE TABLE `write-ds-0`.`t_order_1` LIKE `ds`.`t_order`;
CREATE TABLE `write-ds-0-read-0`.`t_order_0` LIKE `ds`.`t_order`;
CREATE TABLE `write-ds-0-read-0`.`t_order_1` LIKE `ds`.`t_order`;
CREATE TABLE `write-ds-0-read-1`.`t_order_0` LIKE `ds`.`t_order`;
CREATE TABLE `write-ds-0-read-1`.`t_order_1` LIKE `ds`.`t_order`;

CREATE TABLE `write-ds-1`.`t_order_0` LIKE `ds`.`t_order`;
CREATE TABLE `write-ds-1`.`t_order_1` LIKE `ds`.`t_order`;
CREATE TABLE `write-ds-1-read-0`.`t_order_0` LIKE `ds`.`t_order`;
CREATE TABLE `write-ds-1-read-0`.`t_order_1` LIKE `ds`.`t_order`;
CREATE TABLE `write-ds-1-read-1`.`t_order_0` LIKE `ds`.`t_order`;
CREATE TABLE `write-ds-1-read-1`.`t_order_1` LIKE `ds`.`t_order`;

CREATE TABLE `write-ds-0`.`t_order_item_0` LIKE `ds`.`t_order_item`;
CREATE TABLE `write-ds-0`.`t_order_item_1` LIKE `ds`.`t_order_item`;
CREATE TABLE `write-ds-0-read-0`.`t_order_item_0` LIKE `ds`.`t_order_item`;
CREATE TABLE `write-ds-0-read-0`.`t_order_item_1` LIKE `ds`.`t_order_item`;
CREATE TABLE `write-ds-0-read-1`.`t_order_item_0` LIKE `ds`.`t_order_item`;
CREATE TABLE `write-ds-0-read-1`.`t_order_item_1` LIKE `ds`.`t_order_item`;

CREATE TABLE `write-ds-1`.`t_order_item_0` LIKE `ds`.`t_order_item`;
CREATE TABLE `write-ds-1`.`t_order_item_1` LIKE `ds`.`t_order_item`;
CREATE TABLE `write-ds-1-read-0`.`t_order_item_0` LIKE `ds`.`t_order_item`;
CREATE TABLE `write-ds-1-read-0`.`t_order_item_1` LIKE `ds`.`t_order_item`;
CREATE TABLE `write-ds-1-read-1`.`t_order_item_0` LIKE `ds`.`t_order_item`;
CREATE TABLE `write-ds-1-read-1`.`t_order_item_1` LIKE `ds`.`t_order_item`;

--

TRUNCATE TABLE `write-ds-0`.`t_order_0`;
TRUNCATE TABLE `write-ds-0`.`t_order_1`;
TRUNCATE TABLE `write-ds-0-read-0`.`t_order_0`;
TRUNCATE TABLE `write-ds-0-read-0`.`t_order_1`;
TRUNCATE TABLE `write-ds-0-read-1`.`t_order_0`;
TRUNCATE TABLE `write-ds-0-read-1`.`t_order_1`;

TRUNCATE TABLE `write-ds-1`.`t_order_0`;
TRUNCATE TABLE `write-ds-1`.`t_order_1`;
TRUNCATE TABLE `write-ds-1-read-0`.`t_order_0`;
TRUNCATE TABLE `write-ds-1-read-0`.`t_order_1`;
TRUNCATE TABLE `write-ds-1-read-1`.`t_order_0`;
TRUNCATE TABLE `write-ds-1-read-1`.`t_order_1`;

TRUNCATE TABLE `write-ds-0`.`t_order_item_0`;
TRUNCATE TABLE `write-ds-0`.`t_order_item_1`;
TRUNCATE TABLE `write-ds-0-read-0`.`t_order_item_0`;
TRUNCATE TABLE `write-ds-0-read-0`.`t_order_item_1`;
TRUNCATE TABLE `write-ds-0-read-1`.`t_order_item_0`;
TRUNCATE TABLE `write-ds-0-read-1`.`t_order_item_1`;

TRUNCATE TABLE `write-ds-1`.`t_order_item_0`;
TRUNCATE TABLE `write-ds-1`.`t_order_item_1`;
TRUNCATE TABLE `write-ds-1-read-0`.`t_order_item_0`;
TRUNCATE TABLE `write-ds-1-read-0`.`t_order_item_1`;
TRUNCATE TABLE `write-ds-1-read-1`.`t_order_item_0`;
TRUNCATE TABLE `write-ds-1-read-1`.`t_order_item_1`;

--

drop schema if exists `ds`;
drop schema if exists `write-ds-0`;
drop schema if exists `write-ds-0-read-0`;
drop schema if exists `write-ds-0-read-1`;
drop schema if exists `write-ds-1`;
drop schema if exists `write-ds-1-read-0`;
drop schema if exists `write-ds-1-read-1`;
