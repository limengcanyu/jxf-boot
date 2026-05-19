package org.asura.ddd.structure.inventory.domain.service;

import org.asura.ddd.structure.inventory.domain.model.aggregate.Inventory;
import org.asura.ddd.structure.inventory.domain.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryDomainService {

    private final InventoryRepository inventoryRepository;

    public InventoryDomainService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Inventory createInventory(String productId, Integer quantity) {
        if (inventoryRepository.findByProductId(productId).isPresent()) {
            throw new IllegalArgumentException("Inventory already exists for product: " + productId);
        }
        Inventory inventory = Inventory.create(productId, quantity);
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory increaseStock(String productId, Integer amount) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for product: " + productId));
        inventory.increase(amount);
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory decreaseStock(String productId, Integer amount) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for product: " + productId));
        inventory.decrease(amount);
        return inventoryRepository.save(inventory);
    }

    public Inventory getInventoryByProductId(String productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for product: " + productId));
    }

    public Integer getAvailableStock(String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for product: " + productId));
        return inventory.getAvailableQuantity();
    }
}