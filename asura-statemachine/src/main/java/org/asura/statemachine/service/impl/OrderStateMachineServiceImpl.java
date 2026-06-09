package org.asura.statemachine.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.statemachine.domain.Order;
import org.asura.statemachine.enums.OrderEvent;
import org.asura.statemachine.enums.OrderStatus;
import org.asura.statemachine.persist.InMemoryStateMachinePersister;
import org.asura.statemachine.service.OrderStateMachineService;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStateMachineServiceImpl implements OrderStateMachineService {

    private final StateMachineFactory<OrderStatus, OrderEvent> stateMachineFactory;
    private final InMemoryStateMachinePersister stateMachinePersister;

    private final Map<String, Order> orderStorage = new ConcurrentHashMap<>();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
            order.setOrderId(java.util.UUID.randomUUID().toString());
        }
        order.setStatus(OrderStatus.CREATED.getCode());
        order.setCreatedTime(LocalDateTime.now());
        order.setUpdatedTime(LocalDateTime.now());

        orderStorage.put(order.getOrderId(), order);
        log.info("创建订单成功，订单ID: {}", order.getOrderId());

        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order handleEvent(String orderId, OrderEvent event) {
        Order order = orderStorage.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在，订单ID: " + orderId);
        }

        OrderStatus currentStatus = OrderStatus.fromCode(order.getStatus());
        log.info("处理订单事件，订单ID: {}, 当前状态: {}, 事件: {}", orderId, currentStatus, event);
        
        StateMachine<OrderStatus, OrderEvent> stateMachine = stateMachineFactory.getStateMachine(orderId);

        try {
            stateMachine.getExtendedState().getVariables().put("orderId", orderId);
            stateMachine.startReactively().block();

            OrderStatus savedState = stateMachinePersister.getSavedState(orderId);
            if (savedState != null && !savedState.equals(currentStatus)) {
                log.warn("持久化状态与订单状态不一致，使用订单状态，订单ID: {}, 持久化状态: {}, 订单状态: {}", 
                        orderId, savedState, currentStatus);
            }

            if (currentStatus != OrderStatus.CREATED) {
                replayEvents(stateMachine, currentStatus);
            }

            Flux<StateMachineEventResult<OrderStatus, OrderEvent>> resultFlux = 
                stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(event).build()));
            
            StateMachineEventResult<OrderStatus, OrderEvent> result = resultFlux.blockFirst();
            
            log.info("状态机发送事件结果: {}, 当前状态: {}", result, stateMachine.getState());
            
            if (result == null || result.getResultType() != StateMachineEventResult.ResultType.ACCEPTED) {
                throw new IllegalStateException(String.format(
                        "无法执行事件 [%s]，当前状态 [%s]",
                        event.getDescription(),
                        currentStatus.getDescription()
                ));
            }

            OrderStatus newStatus = stateMachine.getState().getId();
            order.setStatus(newStatus.getCode());
            order.setUpdatedTime(LocalDateTime.now());

            try {
                stateMachinePersister.persist(stateMachine, orderId);
            } catch (Exception e) {
                log.error("持久化状态机失败，订单ID: {}", orderId, e);
            }
            log.info("状态转换成功，订单ID: {}, 事件: {}, 新状态: {}",
                    orderId, event, newStatus);

            return order;
        } finally {
            stateMachine.stopReactively().block();
        }
    }

    private void replayEvents(StateMachine<OrderStatus, OrderEvent> stateMachine, OrderStatus targetStatus) {
        if (targetStatus == OrderStatus.PAID) {
            sendEvent(stateMachine, OrderEvent.PAY);
        } else if (targetStatus == OrderStatus.SHIPPED) {
            sendEvent(stateMachine, OrderEvent.PAY);
            sendEvent(stateMachine, OrderEvent.SHIP);
        } else if (targetStatus == OrderStatus.DELIVERED) {
            sendEvent(stateMachine, OrderEvent.PAY);
            sendEvent(stateMachine, OrderEvent.SHIP);
            sendEvent(stateMachine, OrderEvent.DELIVER);
        } else if (targetStatus == OrderStatus.COMPLETED) {
            sendEvent(stateMachine, OrderEvent.PAY);
            sendEvent(stateMachine, OrderEvent.SHIP);
            sendEvent(stateMachine, OrderEvent.DELIVER);
            sendEvent(stateMachine, OrderEvent.COMPLETE);
        } else if (targetStatus == OrderStatus.CANCELLED) {
            sendEvent(stateMachine, OrderEvent.CANCEL);
        } else if (targetStatus == OrderStatus.REFUNDED) {
            sendEvent(stateMachine, OrderEvent.PAY);
            sendEvent(stateMachine, OrderEvent.REFUND);
        }
    }

    private void sendEvent(StateMachine<OrderStatus, OrderEvent> stateMachine, OrderEvent event) {
        Flux<StateMachineEventResult<OrderStatus, OrderEvent>> resultFlux = 
            stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(event).build()));
        resultFlux.blockFirst();
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderStorage.get(orderId);
    }

}