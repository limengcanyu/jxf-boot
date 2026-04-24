package com.spring.boot.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="person")
@Data
public class Person {
    @Id
    private String id;
    private String name;
    private int age;
    private String expression;

    /**
     * 乐观锁字段
     */
    @Version
    Long version;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(String name, int age, String expression) {
        this.name = name;
        this.age = age;
        this.expression = expression;
    }
}
