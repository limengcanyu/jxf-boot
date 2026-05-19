//package com.spring.boot.tomcat.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaSendingMessage {
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    public void send() {
//        kafkaTemplate.send("topic1", "hello kafka");
//    }
//
//}
