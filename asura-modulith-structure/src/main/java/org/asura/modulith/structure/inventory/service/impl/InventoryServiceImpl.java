package org.asura.modulith.structure.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.asura.modulith.structure.inventory.dto.AddProductDTO;
import org.asura.modulith.structure.inventory.entity.Inventory;
import org.asura.modulith.structure.inventory.mapper.InventoryMapper;
import org.asura.modulith.structure.inventory.service.InventoryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryMapper inventoryMapper;

    @Override
    public void addProduct(AddProductDTO addProductDTO) {
        Inventory inventory = new Inventory(addProductDTO.productId(), addProductDTO.quantity());
        inventoryMapper.addProduct(inventory);
    }

}
