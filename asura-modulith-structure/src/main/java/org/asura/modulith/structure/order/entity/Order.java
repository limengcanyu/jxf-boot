package org.asura.modulith.structure.order.entity;

import lombok.Data;

@Data
public class Order {
    private Long id;
    private Long userId;
    private String orderNo;
    private Integer goodsNum;
}
