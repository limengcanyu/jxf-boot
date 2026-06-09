package org.asura.statemachine.persist;

import lombok.extern.slf4j.Slf4j;
import org.asura.statemachine.enums.OrderEvent;
import org.asura.statemachine.enums.OrderStatus;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class InMemoryStateMachinePersister implements StateMachinePersister<OrderStatus, OrderEvent, String> {

    private final Map<String, OrderStatus> storage = new ConcurrentHashMap<>();

    @Override
    public void persist(StateMachine<OrderStatus, OrderEvent> stateMachine, String contextObj) {
        OrderStatus state = stateMachine.getState().getId();
        storage.put(contextObj, state);
        log.debug("持久化状态机，上下文: {}, 状态: {}", contextObj, state);
    }

    @Override
    public StateMachine<OrderStatus, OrderEvent> restore(StateMachine<OrderStatus, OrderEvent> stateMachine, String contextObj) {
        log.debug("恢复状态机，上下文: {}", contextObj);
        return stateMachine;
    }

    public OrderStatus getSavedState(String orderId) {
        return storage.get(orderId);
    }

}