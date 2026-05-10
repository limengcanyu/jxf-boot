package org.asura.modulith.structure.inventory.mapper;

import org.asura.modulith.structure.inventory.entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public void decrease(String productId, Integer quantity) {
        // 减少库存
        // ...
    }

    public void addProduct(Inventory inventory) {
        // 添加库存
        // ...
    }
}
