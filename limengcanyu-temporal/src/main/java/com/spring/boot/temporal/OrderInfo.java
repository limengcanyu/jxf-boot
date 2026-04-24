package com.spring.boot.temporal;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order_info")
public class OrderInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderId;
    private Double amount;

    @TableField("`status`")
    private String status; // 如: CREATED, PAYMENT_SUCCESS, RISK_PENDING, SUCCESS, FAILED

    private String reason; // 失败原因

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private boolean exists;
}
