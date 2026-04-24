package com.spring.boot.kafka.entity;

import lombok.Data;

@Data
public class Person {
    private Integer id;
    private String name;
    private String address;

    /**
     * 消费者反序列化时会使用默认构造器，必须提供
     */
    public Person() {
    }

    public Person(Integer id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
}
