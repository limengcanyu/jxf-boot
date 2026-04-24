package com.spring.boot.kafka.streams;

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
        kafkaTemplate.send("ks1In", "ks1In message 000001");
        kafkaTemplate.send("ks1In", "ks1In message 000002");

    }

}
