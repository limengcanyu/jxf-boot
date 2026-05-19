package org.asura.ddd.structure.inventory.infrastructure.controller;

import org.asura.ddd.structure.common.dto.response.ApiResponse;
import org.asura.ddd.structure.inventory.application.dto.command.InventoryAdjustCommand;
import org.asura.ddd.structure.inventory.application.dto.command.StockAdjustCommand;
import org.asura.ddd.structure.inventory.application.dto.query.InventoryPageQuery;
import org.asura.ddd.structure.inventory.application.dto.query.InventoryQuery;
import org.asura.ddd.structure.inventory.application.dto.response.InventoryResponse;
import org.asura.ddd.structure.inventory.application.service.InventoryApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    private final InventoryApplicationService inventoryApplicationService;

    public InventoryController(InventoryApplicationService inventoryApplicationService) {
        this.inventoryApplicationService = inventoryApplicationService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> create(@RequestBody InventoryAdjustCommand command) {
        InventoryResponse response = inventoryApplicationService.createInventory(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponse> getByProductId(@PathVariable String productId) {
        InventoryResponse response = inventoryApplicationService.getInventory(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}/available")
    public ResponseEntity<Integer> getAvailableStock(@PathVariable String productId) {
        Integer availableStock = inventoryApplicationService.getAvailableStock(productId);
        return ResponseEntity.ok(availableStock);
    }

    @PutMapping("/increase")
    public ResponseEntity<InventoryResponse> increaseStock(@RequestBody StockAdjustCommand command) {
        InventoryResponse response = inventoryApplicationService.increaseStock(command);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/decrease")
    public ResponseEntity<InventoryResponse> decreaseStock(@RequestBody StockAdjustCommand command) {
        InventoryResponse response = inventoryApplicationService.decreaseStock(command);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> delete(@PathVariable String productId) {
        inventoryApplicationService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<InventoryResponse>> queryPage(@ModelAttribute InventoryPageQuery query) {
        ApiResponse<InventoryResponse> response = inventoryApplicationService.queryPage(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<InventoryResponse>> queryList(@ModelAttribute InventoryQuery query) {
        List<InventoryResponse> response = inventoryApplicationService.queryList(query);
        return ResponseEntity.ok(response);
    }
}