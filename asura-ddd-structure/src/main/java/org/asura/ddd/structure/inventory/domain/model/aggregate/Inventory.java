package org.asura.ddd.structure.inventory.domain.model.aggregate;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Inventory {

    private String id;
    private String productId;
    private Integer quantity;
    private Integer reservedQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Inventory() {
    }

    public static Inventory create(String productId, Integer quantity) {
        Inventory inventory = new Inventory();
        inventory.id = UUID.randomUUID().toString();
        inventory.productId = productId;
        inventory.quantity = quantity;
        inventory.reservedQuantity = 0;
        inventory.createdAt = LocalDateTime.now();
        inventory.updatedAt = LocalDateTime.now();
        return inventory;
    }

    public void increase(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.quantity += amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void decrease(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.quantity < amount) {
            throw new IllegalStateException("Insufficient inventory");
        }
        this.quantity -= amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void reserve(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        int available = this.quantity - this.reservedQuantity;
        if (available < amount) {
            throw new IllegalStateException("Insufficient available inventory");
        }
        this.reservedQuantity += amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void releaseReservation(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("Cannot release more than reserved");
        }
        this.reservedQuantity -= amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void confirmReservation(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("Cannot confirm more than reserved");
        }
        this.reservedQuantity -= amount;
        this.quantity -= amount;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getAvailableQuantity() {
        return this.quantity - this.reservedQuantity;
    }

}