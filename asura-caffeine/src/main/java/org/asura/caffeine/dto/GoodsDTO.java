package org.asura.caffeine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品实体DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDTO {
    private Long goodsId;
    private String goodsName;
    private BigDecimal price;
    private Integer stock;
}
