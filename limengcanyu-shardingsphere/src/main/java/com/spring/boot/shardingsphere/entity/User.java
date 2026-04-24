package com.spring.boot.shardingsphere.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.LocalDateTypeHandler;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author rock
 * @since 2022-06-15
 */
@Data
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一ID
     */
    @TableId
    private Long userId;

//    /**
//     * 名字
//     */
//    private String fullname;
//
//    /**
//     * 年龄
//     */
//    private Integer age;
//
//    /**
//     * 出生日期
//     */
//    private LocalDate birthday;
//
//    /**
//     * 密码
//     */
//    private String pwd;
//
//    /**
//     * 手机号
//     */
//    private String mobile;
//
//    /**
//     * 身份证
//     */
//    private String idCard;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 版本号
     */
    private Integer version;

}
