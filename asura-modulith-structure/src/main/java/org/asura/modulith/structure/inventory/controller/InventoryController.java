package org.asura.modulith.structure.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.asura.modulith.structure.inventory.dto.AddProductDTO;
import org.asura.modulith.structure.inventory.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/inventory")
@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * http://localhost:8080/inventory/AddProduct
     */
    @PostMapping("/AddProduct")
    public void addProduct(@RequestBody AddProductDTO addProductDTO) {
        inventoryService.addProduct(addProductDTO);
    }

}
