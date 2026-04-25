package org.asura.mongodb.repository;

import org.asura.mongodb.entity.LabelInventory;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelInventoryRepository extends CommonMongoRepository<LabelInventory, String> {
}
