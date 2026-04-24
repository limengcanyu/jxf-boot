package com.spring.boot.mybatisplus.druid.multi.datasource.valeera.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统订单表
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-07-24
 */
@Data
@Accessors(chain = true)
@TableName("sys_order")
public class SysOrder extends Model<SysOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;
    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 下单时间
     */
    @TableField("order_time")
    private Date orderTime;


    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

}
