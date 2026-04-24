package com.spring.boot.kafka.entity;

import lombok.Data;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * date 2019/06/21
 */
@Data
public class Role {
    private String roleId;
    private String roleName;

    public Role() {
    }

    public Role(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
}
