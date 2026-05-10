package org.asura.modulith.structure.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateOrderDTO {
    private Long orderId;
    private String orderNumber;
}
