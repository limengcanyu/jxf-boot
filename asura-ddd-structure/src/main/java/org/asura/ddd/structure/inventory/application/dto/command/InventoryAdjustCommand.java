package org.asura.ddd.structure.inventory.application.dto.command;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventoryAdjustCommand {

    private String productId;
    private Integer quantity;

    public InventoryAdjustCommand() {
    }

    public InventoryAdjustCommand(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

}