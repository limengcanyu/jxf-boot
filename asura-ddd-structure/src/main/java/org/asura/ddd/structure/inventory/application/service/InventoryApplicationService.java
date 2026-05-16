package org.asura.ddd.structure.inventory.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.asura.ddd.structure.common.dto.response.ApiResponse;
import org.asura.ddd.structure.inventory.application.dto.command.InventoryAdjustCommand;
import org.asura.ddd.structure.inventory.application.dto.command.StockAdjustCommand;
import org.asura.ddd.structure.inventory.application.dto.query.InventoryPageQuery;
import org.asura.ddd.structure.inventory.application.dto.query.InventoryQuery;
import org.asura.ddd.structure.inventory.application.dto.response.InventoryResponse;
import org.asura.ddd.structure.inventory.domain.model.aggregate.Inventory;
import org.asura.ddd.structure.inventory.domain.repository.InventoryRepository;
import org.asura.ddd.structure.inventory.domain.service.InventoryDomainService;
import org.asura.ddd.structure.inventory.infrastructure.repository.InventoryRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryApplicationService {

    private final InventoryDomainService inventoryDomainService;
    private final InventoryRepository inventoryRepository;
    private final InventoryRepositoryImpl inventoryRepositoryImpl;

    public InventoryApplicationService(InventoryDomainService inventoryDomainService,
                                       InventoryRepository inventoryRepository,
                                       InventoryRepositoryImpl inventoryRepositoryImpl) {
        this.inventoryDomainService = inventoryDomainService;
        this.inventoryRepository = inventoryRepository;
        this.inventoryRepositoryImpl = inventoryRepositoryImpl;
    }

    public InventoryResponse createInventory(InventoryAdjustCommand command) {
        Inventory inventory = inventoryDomainService.createInventory(command.getProductId(), command.getQuantity());
        return InventoryResponse.from(inventory);
    }

    public InventoryResponse increaseStock(StockAdjustCommand command) {
        Inventory inventory = inventoryDomainService.increaseStock(command.getProductId(), command.getQuantity());
        return InventoryResponse.from(inventory);
    }

    public InventoryResponse decreaseStock(StockAdjustCommand command) {
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

    public void delete(String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        inventoryRepository.deleteById(inventory.getId());
    }

    public ApiResponse<InventoryResponse> queryPage(InventoryPageQuery query) {
        IPage<Inventory> page = inventoryRepositoryImpl.findPage(
                query.getPageNum(), 
                query.getPageSize(), 
                query.getProductId()
        );
        IPage<InventoryResponse> responsePage = page.convert(InventoryResponse::from);
        return ApiResponse.success(responsePage.getRecords(), responsePage);
    }

    public List<InventoryResponse> queryList(InventoryQuery query) {
        return inventoryRepositoryImpl.findList(query.getProductId())
                .stream()
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }
}