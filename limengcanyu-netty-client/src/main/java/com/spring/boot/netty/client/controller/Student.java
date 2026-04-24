package com.spring.boot.netty.client.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Student {

    private String id;

    private Integer age;

    private String name;

    private Date createTime;

    public Student(Integer age, String name) {
        this.age = age;
        this.name = name;
    }
}