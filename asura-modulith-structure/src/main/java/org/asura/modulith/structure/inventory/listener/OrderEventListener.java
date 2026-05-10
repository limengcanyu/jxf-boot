package org.asura.modulith.structure.inventory.listener;

import lombok.RequiredArgsConstructor;
import org.asura.modulith.structure.inventory.mapper.InventoryMapper;
import org.asura.modulith.structure.shared.event.order.OrderCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class OrderEventListener {

    private final InventoryMapper inventoryMapper;

    @Transactional(rollbackFor = Exception.class)
    @EventListener
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserPlaceOrder(OrderCreatedEvent event){
        System.out.println("Inventory 收到订单创建事件：" + event);
        inventoryMapper.decrease(event.productId(), event.quantity());
    }

}
