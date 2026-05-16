package org.asura.ddd.structure.order.domain.service;

import org.asura.ddd.structure.inventory.domain.model.aggregate.Inventory;
import org.asura.ddd.structure.inventory.domain.repository.InventoryRepository;
import org.asura.ddd.structure.order.domain.model.aggregate.Order;
import org.asura.ddd.structure.order.domain.model.entity.OrderItem;
import org.asura.ddd.structure.order.domain.model.valueobject.ShippingAddress;
import org.asura.ddd.structure.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDomainService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;

    public OrderDomainService(OrderRepository orderRepository, InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Order createOrder(String userId, ShippingAddress shippingAddress, List<OrderItem> items) {
        Order order = Order.create(userId, shippingAddress);

        for (OrderItem item : items) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not in inventory: " + item.getProductId()));
            inventory.reserve(item.getQuantity());
            inventoryRepository.save(inventory);
            order.addItem(item);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.confirm();
        return orderRepository.save(order);
    }

    @Transactional
    public Order payOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.pay();

        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
            inventory.confirmReservation(item.getQuantity());
            inventoryRepository.save(inventory);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order shipOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.ship();
        return orderRepository.save(order);
    }

    @Transactional
    public Order completeOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.complete();
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.cancel();

        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
            inventory.releaseReservation(item.getQuantity());
            inventoryRepository.save(inventory);
        }

        return orderRepository.save(order);
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}