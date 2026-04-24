package com.spring.boot.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * <p>Description: KafkaTemplate Test</p>
 *
 * @author rock.jiang
 * date 2019/06/21
 */
@SpringBootTest
public class KafkaTemplateTest {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Test
    public void sendStringMessage() {
        kafkaTemplate.send("my-replicated-topic", "my-replicated-topic message 000001");
        kafkaTemplate.send("my-replicated-topic", "my-replicated-topic message 000002");

    }

    @Test
    public void sendPOJOMessage() {
//        kafkaTemplate.send("userTopic", new User("userId_003", "userName", "1234567890"));
//        kafkaTemplate.send("userTopic", new User("userId_004", "userName", "1234567890"));
    }
}
