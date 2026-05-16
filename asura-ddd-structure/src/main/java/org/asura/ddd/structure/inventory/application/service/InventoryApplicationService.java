package org.asura.ddd.structure.inventory.application.service;

import org.asura.ddd.structure.inventory.application.dto.command.InventoryAdjustCommand;
import org.asura.ddd.structure.inventory.application.dto.response.InventoryResponse;
import org.asura.ddd.structure.inventory.domain.model.aggregate.Inventory;
import org.asura.ddd.structure.inventory.domain.service.InventoryDomainService;
import org.springframework.stereotype.Service;

@Service
public class InventoryApplicationService {

    private final InventoryDomainService inventoryDomainService;

    public InventoryApplicationService(InventoryDomainService inventoryDomainService) {
        this.inventoryDomainService = inventoryDomainService;
    }

    public InventoryResponse createInventory(InventoryAdjustCommand command) {
        Inventory inventory = inventoryDomainService.createInventory(command.getProductId(), command.getQuantity());
        return InventoryResponse.from(inventory);
    }

    public InventoryResponse increaseStock(InventoryAdjustCommand command) {
        Inventory inventory = inventoryDomainService.increaseStock(command.getProductId(), command.getQuantity());
        return InventoryResponse.from(inventory);
    }

    public InventoryResponse decreaseStock(InventoryAdjustCommand command) {
        Inventory inventory = inventoryDomainService.decreaseStock(command.getProductId(), command.getQuantity());
        return InventoryResponse.from(inventory);
    }

    public InventoryResponse getInventory(String productId) {
        Inventory inventory = inventoryDomainService.getInventoryByProductId(productId);
        return InventoryResponse.from(inventory);
    }

    public Integer getAvailableStock(String productId) {
        return inventoryDomainService.getAvailableStock(productId);
    }
}