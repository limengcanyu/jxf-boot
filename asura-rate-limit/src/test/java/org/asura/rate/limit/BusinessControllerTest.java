package org.asura.rate.limit;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class BusinessControllerTest {

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void businessApi() {
        for (int i = 0; i < 100; i++) {
            restTemplate.getForEntity("http://localhost:8080/api/business", String.class);
        }
    }

}
