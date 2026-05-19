package org.asura.ddd.structure.inventory.application.dto.response;

import org.asura.ddd.structure.inventory.domain.model.aggregate.Inventory;

import java.time.format.DateTimeFormatter;

public class InventoryResponse {

    private String id;
    private String productId;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private String createdAt;
    private String updatedAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public InventoryResponse() {
    }

    public static InventoryResponse from(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.id = inventory.getId();
        response.productId = inventory.getProductId();
        response.quantity = inventory.getQuantity();
        response.reservedQuantity = inventory.getReservedQuantity();
        response.availableQuantity = inventory.getAvailableQuantity();
        response.createdAt = inventory.getCreatedAt() != null ? inventory.getCreatedAt().format(FORMATTER) : null;
        response.updatedAt = inventory.getUpdatedAt() != null ? inventory.getUpdatedAt().format(FORMATTER) : null;
        return response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}