package org.asura.ddd.structure.inventory.domain.repository;

import org.asura.ddd.structure.inventory.domain.model.aggregate.Inventory;

import java.util.Optional;

public interface InventoryRepository {

    Inventory save(Inventory inventory);

    Optional<Inventory> findById(String id);

    Optional<Inventory> findByProductId(String productId);

    void deleteById(String id);
}