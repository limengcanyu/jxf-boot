package com.spring.boot.data.mongodb.repository;

import com.spring.boot.data.mongodb.entity.LabelInventory;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelInventoryRepository extends CommonMongoRepository<LabelInventory, String> {
}
