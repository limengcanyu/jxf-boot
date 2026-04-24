package com.spring.boot.mybatisplus.sharding.jdbc.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-05-25
 */
@Data
@Accessors(chain = true)
@TableName("t_order")
public class Order extends Model<Order> {

    private static final long serialVersionUID = 1L;

    @TableId("order_id")
    private Integer orderId;
    @TableField("user_id")
    private Integer userId;
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

}
