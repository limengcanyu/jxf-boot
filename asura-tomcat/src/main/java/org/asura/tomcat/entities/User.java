package org.asura.tomcat.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private String userId;
    private String username;
    private String password;
    private int age;
}
