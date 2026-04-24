package com.spring.boot.data.mongodb.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Date updateTime;

}
