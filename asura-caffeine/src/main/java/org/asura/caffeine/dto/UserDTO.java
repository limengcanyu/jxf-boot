package org.asura.caffeine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Integer age;
    private String phone;

    public UserDTO(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public UserDTO(Long userId, String userName, Integer age, String phone) {
        this.userId = userId;
        this.userName = userName;
        this.age = age;
        this.phone = phone;
    }

}
