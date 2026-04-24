package org.akuma.actuator.dto;

import lombok.Data;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/30 0030
 */
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
