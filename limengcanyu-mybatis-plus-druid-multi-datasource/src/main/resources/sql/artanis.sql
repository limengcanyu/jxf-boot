CREATE TABLE `sys_user` (
  `user_id`         int(11)        NOT NULL       AUTO_INCREMENT COMMENT '用户ID',
  `username`        varchar(45)    NOT NULL                      COMMENT '用户名',
  `password`        varchar(45)    NOT NULL                      COMMENT '密码',
  `chinese_name`    varchar(45)    DEFAULT NULL                  COMMENT '中文名称',
  `sex`             tinyint(1)     DEFAULT NULL                  COMMENT '性别：1-男，2-女',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户表';


CREATE TABLE `sys_role` (
  `role_id`         int(11)        NOT NULL       AUTO_INCREMENT COMMENT '角色ID',
  `role_name`       varchar(45)    NOT NULL                      COMMENT '角色名称',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统角色表';


CREATE TABLE `sys_user_role` (
  `user_role_id`    int(11)        NOT NULL       AUTO_INCREMENT COMMENT '用户角色ID',
  `user_id`         int(11)        NOT NULL                      COMMENT '用户ID',
  `role_id`         int(11)        NOT NULL                      COMMENT '角色ID',
  PRIMARY KEY (`user_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户角色表';


CREATE TABLE `sys_permission` (
  `permission_id`   int(11)        NOT NULL       AUTO_INCREMENT COMMENT '权限ID',
  `permission_name` varchar(45)    NOT NULL                      COMMENT '权限名称',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统权限表';


CREATE TABLE `sys_role_permission` (
  `role_permission_id` int(11)     NOT NULL       AUTO_INCREMENT COMMENT '角色权限ID',
  `role_id`            int(11)     NOT NULL                      COMMENT '角色ID',
  `permission_id`      int(11)     NOT NULL                      COMMENT '权限ID',
  PRIMARY KEY (`role_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统角色权限表';


CREATE TABLE `sys_company` (
  `company_id`         int(11)        NOT NULL       AUTO_INCREMENT COMMENT '公司ID',
  `company_number`     varchar(45)    NOT NULL                      COMMENT '公司编号',
  `chinese_name`       varchar(45)    DEFAULT NULL                  COMMENT '中文名称',
  `english_name`       varchar(45)    DEFAULT NULL                  COMMENT '英文名称',
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统公司表';


INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('artanis',  '123456', '阿塔尼斯',   1);
INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('varian',   '123456', '瓦里安',     1);
INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('sylvanas', '123456', '希尔瓦娜斯', 2);
INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('rexxar ',  '123456', '雷克萨',     1);
INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('selendis', '123456', '瑟兰迪斯',   2);
INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('karass',   '123456', '卡拉斯',     1);
INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('kerrigan', '123456', '凯瑞甘',     2);
INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('tychus',   '123456', '泰凯斯',     1);
INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('zeratul',  '123456', '泽拉图',     1);
INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('tassadar', '123456', '塔萨达尔',   1);
INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('vorazun',  '123456', '沃拉尊',     2);
INSERT INTO `artanis`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('samuro',   '123456', '萨穆罗',     1);


INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('artanis',  '123456', '阿塔尼斯',   1);
INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('varian',   '123456', '瓦里安',     1);
INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('sylvanas', '123456', '希尔瓦娜斯', 2);
INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('rexxar ',  '123456', '雷克萨',     1);
INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('selendis', '123456', '瑟兰迪斯',   2);
INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('karass',   '123456', '卡拉斯',     1);
INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('kerrigan', '123456', '凯瑞甘',     2);
INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('tychus',   '123456', '泰凯斯',     1);
INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('zeratul',  '123456', '泽拉图',     1);
INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('tassadar', '123456', '塔萨达尔',   1);
INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('vorazun',  '123456', '沃拉尊',     2);
INSERT INTO `samuro`.`sys_user`(`username`,`password`,`chinese_name`,`sex`) VALUES('samuro',   '123456', '萨穆罗',     1);


INSERT INTO `samuro`.`sys_company` (`company_number`,`chinese_name`,`english_name`) VALUES ('CN000001', '上海皓阳股份有限公司', '');















