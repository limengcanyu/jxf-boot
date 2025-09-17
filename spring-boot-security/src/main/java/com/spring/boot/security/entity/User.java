package com.spring.boot.security.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("users") // 指定表名
public class User implements Serializable, UserDetails { // 实现 UserDetails

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username; // 用户名 (唯一)

    @TableField("password")
    private String password; // 加密后的密码

    @TableField("email")
    private String email; // 邮箱

    @TableField("full_name")
    private String fullName; // 全名

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 用于存储用户的角色列表 (非数据库字段)
    @TableField(exist = false)
    private List<String> roles;

    // --- UserDetails 接口方法实现 ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 将 roles (String列表) 转换为 GrantedAuthority 列表
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password; // 返回数据库中存储的加密密码
    }

    @Override
    public String getUsername() {
        return username; // 返回数据库中的用户名
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 根据业务逻辑实现
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 根据业务逻辑实现
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 根据业务逻辑实现
    }

    @Override
    public boolean isEnabled() {
        return true; // 根据业务逻辑实现 (例如，检查用户是否被禁用)
    }
}
