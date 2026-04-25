CREATE TABLE `t_user`
(
    `user_id`       bigint(20) NOT NULL COMMENT '用户唯一ID',
    `fullname`      varchar(50)  DEFAULT NULL COMMENT '名字',
    `age`           int  NOT NULL COMMENT '年龄',
    `birthday`      DATE NOT NULL COMMENT '出生日期',
    `pwd_plain`     varchar(255) DEFAULT NULL COMMENT '明文密码',
    `pwd_cipher`    varchar(255) DEFAULT NULL COMMENT '密文密码',
    `mobile_plain`  varchar(100) DEFAULT NULL COMMENT '明文手机号',
    `mobile_cipher` varchar(100) DEFAULT NULL COMMENT '密文手机号',
    `id_card`       varchar(60)  DEFAULT NULL COMMENT '身份证',
    `version`       int          DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

------------------------------------------------------------------------------------------------------------

------------------------------------------------------------------------------------------------------------

CREATE TABLE `t_address`
(
    `address_id` bigint(20) NOT NULL COMMENT '地址ID',
    `version`    int DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
