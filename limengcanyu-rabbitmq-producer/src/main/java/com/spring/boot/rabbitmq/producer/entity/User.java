package com.spring.boot.rabbitmq.producer.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/03/31 12:13
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -9031549918868850223L;

    private Integer id;
    private String name;
    private Integer age;

    public User() {
    }

    public User(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
