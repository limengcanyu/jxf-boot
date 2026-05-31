package org.asura.statemachine.config;

import lombok.extern.slf4j.Slf4j;
import org.asura.statemachine.enums.OrderEvent;
import org.asura.statemachine.enums.OrderStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStatus, OrderEvent> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states.withStates()
                .initial(OrderStatus.CREATED)
                .states(EnumSet.allOf(OrderStatus.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStatus.CREATED)
                .target(OrderStatus.PAID)
                .event(OrderEvent.PAY)

                .and().withExternal()
                .source(OrderStatus.PAID)
                .target(OrderStatus.SHIPPED)
                .event(OrderEvent.SHIP)

                .and().withExternal()
                .source(OrderStatus.SHIPPED)
                .target(OrderStatus.DELIVERED)
                .event(OrderEvent.DELIVER)

                .and().withExternal()
                .source(OrderStatus.DELIVERED)
                .target(OrderStatus.COMPLETED)
                .event(OrderEvent.COMPLETE)

                .and().withExternal()
                .source(OrderStatus.CREATED)
                .target(OrderStatus.CANCELLED)
                .event(OrderEvent.CANCEL)

                .and().withExternal()
                .source(OrderStatus.PAID)
                .target(OrderStatus.CANCELLED)
                .event(OrderEvent.CANCEL)

                .and().withExternal()
                .source(OrderStatus.PAID)
                .target(OrderStatus.REFUNDED)
                .event(OrderEvent.REFUND);
    }

}