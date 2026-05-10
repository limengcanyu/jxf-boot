package org.asura.modulith.structure.inventory.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Inventory {
    private String productId;
    private Integer quantity;
}
