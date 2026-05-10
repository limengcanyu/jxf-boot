package org.asura.modulith.structure.shared.event.order;

public record OrderCreatedEvent(Long orderId, String productId, Integer quantity) {}
