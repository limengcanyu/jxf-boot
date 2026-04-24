package com.spring.boot.shardingsphere.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author rock
 * @since 2022-06-16
 */
@Data
@TableName("t_address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地址ID
     */
    @TableId
    private Long addressId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 版本号
     */
    private Integer version;

}
