package org.asura.ddd.structure.order.domain.model.aggregate;

import org.asura.ddd.structure.order.domain.model.entity.OrderItem;
import org.asura.ddd.structure.order.domain.model.valueobject.OrderStatus;
import org.asura.ddd.structure.order.domain.model.valueobject.ShippingAddress;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {

    private String id;
    private String userId;
    private List<OrderItem> items;
    private ShippingAddress shippingAddress;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Order() {
        this.items = new ArrayList<>();
    }

    public static Order create(String userId, ShippingAddress shippingAddress) {
        Order order = new Order();
        order.id = UUID.randomUUID().toString();
        order.userId = userId;
        order.shippingAddress = shippingAddress;
        order.totalAmount = BigDecimal.ZERO;
        order.status = OrderStatus.PENDING;
        order.createdAt = LocalDateTime.now();
        order.updatedAt = LocalDateTime.now();
        return order;
    }

    public static Order reconstruct(String id, String userId, ShippingAddress shippingAddress, 
                                   BigDecimal totalAmount, OrderStatus status, 
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        Order order = new Order();
        order.id = id;
        order.userId = userId;
        order.shippingAddress = shippingAddress;
        order.totalAmount = totalAmount;
        order.status = status;
        order.createdAt = createdAt;
        order.updatedAt = updatedAt;
        return order;
    }

    public void addItem(OrderItem item) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot add items to non-pending order");
        }
        this.items.add(item);
        this.totalAmount = this.totalAmount.add(item.getSubtotal());
        this.updatedAt = LocalDateTime.now();
    }

    public void addItemForReconstruct(OrderItem item) {
        this.items.add(item);
    }

    public void removeItem(String productId) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot remove items from non-pending order");
        }
        items.removeIf(item -> item.getProductId().equals(productId));
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be confirmed");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void pay() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed orders can be paid");
        }
        this.status = OrderStatus.PAID;
        this.updatedAt = LocalDateTime.now();
    }

    public void ship() {
        if (status != OrderStatus.PAID) {
            throw new IllegalStateException("Only paid orders can be shipped");
        }
        this.status = OrderStatus.SHIPPED;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        if (status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Only shipped orders can be completed");
        }
        this.status = OrderStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel completed or cancelled order");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}