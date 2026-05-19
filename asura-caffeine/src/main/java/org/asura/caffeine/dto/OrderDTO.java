package org.asura.caffeine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String orderName;
    private Long userId;
    private Long goodsId;
    private BigDecimal amount;
    private LocalDateTime createTime;

    public OrderDTO(Long orderId, String orderName) {
        this.orderId = orderId;
        this.orderName = orderName;
    }

    public OrderDTO(Long orderId, Long userId, Long goodsId, BigDecimal amount, LocalDateTime createTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.goodsId = goodsId;
        this.amount = amount;
        this.createTime = createTime;
    }

}
