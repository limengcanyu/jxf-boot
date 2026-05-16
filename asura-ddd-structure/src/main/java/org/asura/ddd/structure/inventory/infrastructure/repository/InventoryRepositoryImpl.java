package org.asura.ddd.structure.inventory.infrastructure.repository;

import org.asura.ddd.structure.inventory.domain.model.aggregate.Inventory;
import org.asura.ddd.structure.inventory.domain.repository.InventoryRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {

    private final Map<String, Inventory> storage = new ConcurrentHashMap<>();
    private final Map<String, Inventory> productIdIndex = new ConcurrentHashMap<>();

    @Override
    public Inventory save(Inventory inventory) {
        storage.put(inventory.getId(), inventory);
        productIdIndex.put(inventory.getProductId(), inventory);
        return inventory;
    }

    @Override
    public Optional<Inventory> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Inventory> findByProductId(String productId) {
        return Optional.ofNullable(productIdIndex.get(productId));
    }

    @Override
    public void deleteById(String id) {
        Inventory inventory = storage.remove(id);
        if (inventory != null) {
            productIdIndex.remove(inventory.getProductId());
        }
    }
}