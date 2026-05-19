# create schema if not exists artanis;

create table if not exists artanis.platform_salary_item
(
    id                  int auto_increment comment '主键ID'
        primary key,
    item_code           varchar(20)                          null comment '薪资项编码',
    item_name           varchar(200)                         null comment '薪资项名称',
    data_type           tinyint(2) default 0                 null comment '数据类型：1-文本；2-整数；3-小数',
    remark              varchar(255)                         null comment '备注 ',
    is_active           bit        default b'1'              null comment '是否启用：1-启用；0-禁用',
    creator_id          varchar(20)                          null comment '创建人',
    create_time         datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    modifier_id         varchar(20)                          null comment '修改人',
    modify_time         datetime                             null comment '修改时间',
    version             int        default 0                 null comment '版本号'
)
    comment '薪资项定义表';

create index idx_item_code
    on artanis.platform_salary_item (item_code);
