package com.spring.boot.rabbitmq.producer.listener;

import com.rabbitmq.client.ShutdownSignalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.stereotype.Component;

/**
 * <p>Description: Rabbit Connection Listener </p>
 *
 * @author rock.jiang
 * Date 2020/04/01 13:43
 */
@Slf4j
@Component
public class RabbitConnectionListener implements ConnectionListener {

    private RabbitBlockedListener rabbitBlockedListener;

    /**
     * 构造器注入 ConnectionFactory，添加完 BlockedListener 后，将该 ConnectionListener 添加到 ConnectionFactory
     *
     * @param rabbitBlockedListener
     * @param connectionFactory
     */
    public RabbitConnectionListener(RabbitBlockedListener rabbitBlockedListener, ConnectionFactory connectionFactory) {
        this.rabbitBlockedListener = rabbitBlockedListener;
        connectionFactory.addConnectionListener(this);
    }

    @Override
    public void onCreate(Connection connection) {
        log.debug("====== Connection Create Connection: {}", connection);
        connection.addBlockedListener(rabbitBlockedListener);
    }

    @Override
    public void onClose(Connection connection) {
        log.debug("====== Connection Close Connection: {}", connection);
    }

    @Override
    public void onShutDown(ShutdownSignalException signal) {
        log.debug("====== Connection ShutDown ShutdownSignalException: ", signal);
    }
}
