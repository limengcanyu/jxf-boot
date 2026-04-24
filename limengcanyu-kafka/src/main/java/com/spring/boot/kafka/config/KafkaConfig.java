package com.spring.boot.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/04/03 11:23
 */
@Configuration
public class KafkaConfig {
    /**
     * create a topic on startup, if the topic already exists, the bean is ignored.
     *
     * @return
     */
    @Bean
    public NewTopic someTopic() {
        return new NewTopic("someTopic", 1, (short) 1);
    }

    @Bean
    public NewTopic userTopic() {
        return new NewTopic("userTopic", 1, (short) 1);
    }

}
