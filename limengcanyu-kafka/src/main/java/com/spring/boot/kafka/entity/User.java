package com.spring.boot.kafka.entity;

import lombok.Data;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * date 2019/06/21
 */
@Data
public class User {
    private String id;
    private String userId;
    private String userName;
    private String password;

    public User() {
    }

    public User(String userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }
}
