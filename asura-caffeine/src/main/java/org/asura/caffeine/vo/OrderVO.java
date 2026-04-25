package org.asura.caffeine.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long orderId;
    private Long userId;
    private Long goodsId;
    private BigDecimal amount;
    private LocalDateTime createTime;
}

