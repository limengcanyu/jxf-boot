package com.spring.boot.caffeine.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户实体VO (生产规范：序列化+全参/无参构造)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String userName;
    private Integer age;
    private String phone;
}

