package org.asura.ddd.structure.inventory.infrastructure.controller;

import org.asura.ddd.structure.common.dto.response.ApiResponse;
import org.asura.ddd.structure.inventory.application.dto.command.InventoryAdjustCommand;
import org.asura.ddd.structure.inventory.application.dto.response.InventoryResponse;
import org.asura.ddd.structure.inventory.application.service.InventoryApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryApplicationService inventoryApplicationService;

    public InventoryController(InventoryApplicationService inventoryApplicationService) {
        this.inventoryApplicationService = inventoryApplicationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryResponse>> createInventory(@RequestBody InventoryAdjustCommand command) {
        InventoryResponse response = inventoryApplicationService.createInventory(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Inventory created successfully", response));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventory(@PathVariable String productId) {
        InventoryResponse response = inventoryApplicationService.getInventory(productId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{productId}/available")
    public ResponseEntity<ApiResponse<Integer>> getAvailableStock(@PathVariable String productId) {
        Integer available = inventoryApplicationService.getAvailableStock(productId);
        return ResponseEntity.ok(ApiResponse.success(available));
    }

    @PatchMapping("/{productId}/increase")
    public ResponseEntity<ApiResponse<InventoryResponse>> increaseStock(@RequestBody InventoryAdjustCommand command) {
        InventoryResponse response = inventoryApplicationService.increaseStock(command);
        return ResponseEntity.ok(ApiResponse.success("Stock increased", response));
    }

    @PatchMapping("/{productId}/decrease")
    public ResponseEntity<ApiResponse<InventoryResponse>> decreaseStock(@RequestBody InventoryAdjustCommand command) {
        InventoryResponse response = inventoryApplicationService.decreaseStock(command);
        return ResponseEntity.ok(ApiResponse.success("Stock decreased", response));
    }
}