drop schema if exists ds;
create schema ds;

-- t_order表用于MybatisPlus生成代码，生成完之后删掉该表
CREATE TABLE `ds`.`t_order`
(
    `order_id`    bigint(20) NOT NULL COMMENT '订单ID',
    `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
    `version`     int DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ds`.`t_order_0` LIKE `ds`.`t_order`;
CREATE TABLE `ds`.`t_order_1` LIKE `ds`.`t_order`;

--

-- t_order_item表用于MybatisPlus生成代码，生成完之后删掉该表
CREATE TABLE `ds`.`t_order_item`
(
    `order_item_id` bigint(20) NOT NULL COMMENT '订单项ID',
    `order_id`      bigint(20) NOT NULL COMMENT '订单ID',
    `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
    `version`       int DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ds`.`t_order_item_0` LIKE `ds`.`t_order_item`;
CREATE TABLE `ds`.`t_order_item_1` LIKE `ds`.`t_order_item`;

--

drop table `ds`.`t_order`;
drop table `ds`.`t_order_item`;

--

truncate table `ds`.`t_order_0`;
truncate table `ds`.`t_order_1`;
truncate table `ds`.`t_order_item_0`;
truncate table `ds`.`t_order_item_1`;

--

drop schema if exists ds;
