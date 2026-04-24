package com.spring.boot.ssl.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
    @Bean
    RestTemplate template() {
//        // use Apache HttpComponents instead of the native java.net functionality
//        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        return new RestTemplate(new HttpsClientRequestFactory());
    }
}
