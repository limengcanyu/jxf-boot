package com.spring.boot.rabbitmq.producer;

import com.spring.boot.rabbitmq.producer.entity.User;
import com.spring.boot.rabbitmq.producer.listener.RabbitConnectionListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.ReceiveAndReplyCallback;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class SpringBootRabbitmqProducerApplicationTests {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Qualifier("rabbitTemplate")
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

//    @Autowired
//    private TestRabbitTemplate testRabbitTemplate;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitConnectionListener rabbitConnectionListener;

    @Test
    public void getDefaultInfo() {
        log.debug("getExchange: " + rabbitTemplate.getExchange());
        log.debug("getRoutingKey: " + rabbitTemplate.getRoutingKey());

        // org.springframework.amqp.support.converter.SimpleMessageConverter
        log.debug("getMessageConverter: " + rabbitTemplate.getMessageConverter());

    }

    @Test
    public void declareQueue() {
//        amqpAdmin.deleteQueue("testqueue");

        // declare Queue
        amqpAdmin.declareQueue(new Queue("myqueue"));
        // declare Queue
        amqpAdmin.declareQueue(new Queue("sendAndReceiveQueue"));

    }

    @Test
    public void sendingMessage() {
//        amqpAdmin.deleteQueue("testqueue");
//
//        // declare Queue
//        amqpAdmin.declareQueue(new Queue("myqueue"));

        // send 2 message
        amqpTemplate.convertAndSend("myqueue", "foo1");
        amqpTemplate.convertAndSend("myqueue", "foo2");
        amqpTemplate.convertAndSend("myqueue", "foo3");
        log.debug("send message successful");

        // receive 1 message
        String foo = (String) amqpTemplate.receiveAndConvert("myqueue");
        log.debug("receive message: " + foo);

    }

    @Test
    public void receiveAndConvert() {
        rabbitTemplate.convertAndSend("myqueue", "foo1");
        rabbitTemplate.convertAndSend("myqueue", "foo2");

        Object message = rabbitTemplate.receiveAndConvert("myqueue", 5000);
        log.debug("receive message: {}", message);
    }

    @Test
    public void sendingUser() {
        // send 2 message, message must implements Serializable
        amqpTemplate.convertAndSend("userQueue", new User(1, "rock", 1));
        amqpTemplate.convertAndSend("userQueue", new User(2, "blue", 2));
        log.debug("send message successful");

//        // receive 1 message
//        User user1 = (User) amqpTemplate.receiveAndConvert("userQueue");
//        log.debug("receive message: " + user1);

    }

    @Test
    public void sendAndReceive() {
        Object response = amqpTemplate.convertSendAndReceive("sendAndReceiveQueue", "foo1");
        log.debug("send message successful, response is: " + response);
    }

    /**
     * 生产者确认
     */
    @Test
    public void publisherConfirmsAndReturns() {
        // 临时设置，起作用
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.debug("ConfirmCallback correlationData: {} ack: {} cause: {}", correlationData, ack, cause);
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.debug("ReturnCallback message: {} replyCode: {} replyText: {} exchange: {} routingKey: {}", message, replyCode, replyText, exchange, routingKey);
        });
        rabbitTemplate.setUsePublisherConnection(true);

        // add Connection Listener
//        connectionFactory.addConnectionListener(rabbitConnectionListener);


        rabbitTemplate.convertAndSend("myqueue", "foo1");
        // 没有该routingKey输出:
        // ReturnCallback message: (Body:'foo1' MessageProperties [headers={}, contentType=text/plain, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, deliveryTag=0]) replyCode: 312 replyText: NO_ROUTE exchange:  routingKey: myqueue2
        // ConfirmCallback correlationData: null ack: true cause: null

        // 存在该routingKey输出：
        // ConfirmCallback correlationData: null ack: true cause: null

        String foo = (String) amqpTemplate.receiveAndConvert("myqueue");
        log.debug("receive message: " + foo);
    }

    @Test
    public void consumerReceiveAndReply() {
        rabbitTemplate.convertAndSend("myqueue", "foo1");
        rabbitTemplate.convertAndSend("myqueue", "foo2");

        // 消费1条消息，然后将相应消息发送到 sendAndReceiveQueue
        boolean received = rabbitTemplate.receiveAndReply("myqueue", (ReceiveAndReplyCallback<String, String>) payload -> {
            log.debug("payload： {}", payload);
            return "replay message";
        }, rabbitTemplate.getExchange(), "sendAndReceiveQueue");
        if (received) {
            log.info("We received an order!");
        }
    }

    @Test
    public void sendingOrder() {
        rabbitTemplate.convertAndSend("orderQueue", "order1");
    }

    @Test
    public void directExchange() {
        rabbitTemplate.setExchange("directExchange");
        rabbitTemplate.convertAndSend("directExchangeRoutingKey1", "directExchangeRoutingKey1Message");
        rabbitTemplate.convertAndSend("directExchangeRoutingKey2", "directExchangeRoutingKey2Message");

//        Object message = rabbitTemplate.receiveAndConvert("directExchangeQueue1");
//        System.out.println("directExchangeQueue1 receive message: " + message);
    }

    @Test
    public void topicExchange() {
        rabbitTemplate.setExchange("topicExchange");
        rabbitTemplate.convertAndSend("topicExchangeRoutingKey1", "topicExchangeRoutingKey1Message");
        rabbitTemplate.convertAndSend("topicExchangeRoutingKey2", "topicExchangeRoutingKey2Message");
    }

    @Test
    public void fanoutExchange() {
        rabbitTemplate.setExchange("fanoutExchange");
        rabbitTemplate.convertAndSend("fanoutExchangeMessage1");
        rabbitTemplate.convertAndSend("fanoutExchangeMessage2");
    }

    /**
     * 未完成
     */
    @Test
    public void headersExchange() {
        rabbitTemplate.setExchange("headersExchange");
        rabbitTemplate.convertAndSend("headersExchangeMessage1");
        rabbitTemplate.convertAndSend("headersExchangeMessage2");
    }
}
