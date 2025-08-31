package com.spring.boot.mybatis.plus.basic.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jxf
 * @since 2025-08-30
 */
@Getter
@Setter
@ToString
@Schema(name = "User", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 年龄
     */
    @Schema(description = "年龄")
    private Integer age;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;
}
