//package com.spring.boot.tomcat;
//
//import com.spring.boot.tomcat.service.KafkaSendingMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//
///**
// * test using embedded kafka
// */
//@Slf4j
//@SpringBootTest
//@EmbeddedKafka(topics = "topic1", bootstrapServersProperty = "spring.kafka.bootstrap-servers")
//public class KafkaTests {
//    @Autowired
//    private KafkaSendingMessage kafkaSendingMessage;
//
//    @Test
//    public void send() {
//        kafkaSendingMessage.send();
//    }
//}
