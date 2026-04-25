package org.asura.actuator.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    /**
     * 记录ID
     */
    private Long recordId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
