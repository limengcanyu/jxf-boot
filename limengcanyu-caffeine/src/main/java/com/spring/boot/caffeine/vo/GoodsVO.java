package com.spring.boot.caffeine.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品实体VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long goodsId;
    private String goodsName;
    private BigDecimal price;
    private Integer stock;
}

