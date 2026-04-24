package com.spring.boot.kafka;

import com.spring.boot.kafka.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class SpringBootKafkaApplication implements CommandLineRunner {

    public static Logger logger = LoggerFactory.getLogger(SpringBootKafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKafkaApplication.class, args);
    }

    @Autowired
    private KafkaTemplate<String, String> template;

    private final CountDownLatch latch = new CountDownLatch(3);

    @Override
    public void run(String... args) throws Exception {
        this.template.send("myTopic", "foo1");
        this.template.send("myTopic", "foo2");
        this.template.send("myTopic", "foo3");
        latch.await(60, TimeUnit.SECONDS);
        logger.info("All received");
    }

    @KafkaListener(topics = "myTopic")
    public void listen(ConsumerRecord<?, ?> cr) throws Exception {
        logger.info(cr.toString());
        latch.countDown();
    }

    //    @KafkaListener(topics = "my-replicated-topic")
//    public void myReplicatedTopicProcess(String content) {
//        log.debug("my-replicated-topic receive message: {}", content);
//    }

    @KafkaListener(topics = "someTopic")
    public void someTopicProcess(String content) {
        log.debug("someTopic receive message: {}", content);
    }

    /**
     * 直接接收负载对象
     *
     * @param user
     * @throws Exception
     */
    @KafkaListener(topics = "userTopic")
    public void userTopicProcess(User user) {
        log.debug("userTopic receive message: {}", user);
    }

}