package org.asura.ddd.structure.inventory.infrastructure.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.asura.ddd.structure.inventory.domain.model.aggregate.Inventory;
import org.asura.ddd.structure.inventory.domain.repository.InventoryRepository;
import org.asura.ddd.structure.inventory.infrastructure.persistence.entity.InventoryEntity;
import org.asura.ddd.structure.inventory.infrastructure.persistence.mapper.InventoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {

    private final InventoryMapper inventoryMapper;

    public InventoryRepositoryImpl(InventoryMapper inventoryMapper) {
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public Inventory save(Inventory inventory) {
        InventoryEntity entity = toEntity(inventory);
        if (inventoryMapper.selectById(inventory.getId()) != null) {
            inventoryMapper.updateById(entity);
        } else {
            inventoryMapper.insert(entity);
        }
        return inventory;
    }

    @Override
    public Optional<Inventory> findById(String id) {
        InventoryEntity entity = inventoryMapper.selectById(id);
        return Optional.ofNullable(entity).map(this::toDomain);
    }

    @Override
    public Optional<Inventory> findByProductId(String productId) {
        InventoryEntity entity = inventoryMapper.selectByProductId(productId);
        return Optional.ofNullable(entity).map(this::toDomain);
    }

    @Override
    public void deleteById(String id) {
        inventoryMapper.deleteById(id);
    }

    public IPage<Inventory> findPage(int pageNum, int pageSize, String productId) {
        Page<InventoryEntity> page = new Page<>(pageNum, pageSize);
        IPage<InventoryEntity> entityPage = inventoryMapper.selectPageByCondition(page, productId);
        return entityPage.convert(this::toDomain);
    }

    public List<Inventory> findList(String productId) {
        return inventoryMapper.selectPageByCondition(new Page<>(), productId)
                .getRecords()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private InventoryEntity toEntity(Inventory inventory) {
        InventoryEntity entity = new InventoryEntity();
        entity.setId(inventory.getId());
        entity.setProductId(inventory.getProductId());
        entity.setQuantity(inventory.getQuantity());
        entity.setReservedQuantity(inventory.getReservedQuantity());
        entity.setCreatedAt(inventory.getCreatedAt());
        entity.setUpdatedAt(inventory.getUpdatedAt());
        return entity;
    }

    private Inventory toDomain(InventoryEntity entity) {
        return Inventory.reconstruct(
                entity.getId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getReservedQuantity(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}