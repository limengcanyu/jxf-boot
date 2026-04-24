package com.spring.boot.redis.reactive.entity;

import lombok.Data;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/4/12 0012
 */
@Data
public class User {
    private String userId;
    private String username;
    private String password;

    public User() {
    }

    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
}
