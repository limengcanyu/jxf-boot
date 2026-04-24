//package com.spring.boot.data.elasticsearch;
//
//import co.elastic.clients.transport.TransportUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
//
//import javax.net.ssl.SSLContext;
//import java.io.File;
//import java.io.IOException;
//import java.time.Duration;
//
//@Slf4j
//@Configuration
//public class MyClientConfig extends ElasticsearchConfiguration {
//
//    @Override
//    public ClientConfiguration clientConfiguration() {
//        File certFile = new File("E:/IdeaProjects/rock-boot/spring-boot-data-elasticsearch/src/main/resources/cert/http_ca.crt");
//
//        SSLContext sslContext = null;
//        try {
//            sslContext = TransportUtils.sslContextFromHttpCaCrt(certFile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            return ClientConfiguration.builder()
//                    .connectedTo("192.168.242.129:9200", "192.168.242.129:9201", "192.168.242.129:9202")
//                    .usingSsl(sslContext)
//                    .withConnectTimeout(Duration.ofSeconds(5))
//                    .withSocketTimeout(Duration.ofSeconds(3))
//                    .withBasicAuth("elastic", "PassW0rd321")
//                    .build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
