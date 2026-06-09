package org.asura.akka.controller;

import org.asura.akka.TestAkkaApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 分布式计数器控制器集成测试
 * 
 * <p>使用完全独立的测试环境，不依赖集群配置。
 * 
 * @author Asura Team
 * @since 1.0.0
 */
@SpringBootTest(classes = TestAkkaApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class DistributedCounterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/counter";
    private static final String TEST_COUNTER_NAME = "test_counter";

    @BeforeEach
    void setUp() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + TEST_COUNTER_NAME + "/reset"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("测试增量计数器 - 同步")
    void testIncrementCounter() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + TEST_COUNTER_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0));

        mockMvc.perform(post(BASE_URL + "/" + TEST_COUNTER_NAME + "/increment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));

        mockMvc.perform(post(BASE_URL + "/" + TEST_COUNTER_NAME + "/increment")
                        .param("delta", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(6));
    }

    @Test
    @DisplayName("测试异步增量计数器")
    void testIncrementCounterAsync() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + TEST_COUNTER_NAME + "/increment/async"))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/" + TEST_COUNTER_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    @DisplayName("测试获取计数器值")
    void testGetCounterValue() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + TEST_COUNTER_NAME + "/increment")
                        .param("delta", "42"))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/" + TEST_COUNTER_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(42));
    }

    @Test
    @DisplayName("测试异步获取计数器值")
    void testGetCounterValueAsync() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + TEST_COUNTER_NAME + "/increment")
                        .param("delta", "99"))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/" + TEST_COUNTER_NAME + "/async"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("测试批量增量")
    void testBatchIncrement() throws Exception {
        String requestBody = "{\"counter_a\": 10, \"counter_b\": 20, \"counter_c\": 30}";
        
        mockMvc.perform(post(BASE_URL + "/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));

        mockMvc.perform(get(BASE_URL + "/counter_a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10));

        mockMvc.perform(get(BASE_URL + "/counter_b"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(20));

        mockMvc.perform(get(BASE_URL + "/counter_c"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(30));
    }

    @Test
    @DisplayName("测试重置计数器")
    void testResetCounter() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + TEST_COUNTER_NAME + "/increment")
                        .param("delta", "100"))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/" + TEST_COUNTER_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(100));

        mockMvc.perform(post(BASE_URL + "/" + TEST_COUNTER_NAME + "/reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0));

        mockMvc.perform(get(BASE_URL + "/" + TEST_COUNTER_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0));
    }

    @Test
    @DisplayName("测试获取所有计数器")
    void testGetAllCounters() throws Exception {
        mockMvc.perform(post(BASE_URL + "/counter_x/increment").param("delta", "5"))
                .andExpect(status().isOk());
        mockMvc.perform(post(BASE_URL + "/counter_y/increment").param("delta", "15"))
                .andExpect(status().isOk());
        mockMvc.perform(post(BASE_URL + "/counter_z/increment").param("delta", "25"))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    @DisplayName("测试连续增量操作")
    void testMultipleIncrements() throws Exception {
        int iterations = 10;
        int expectedValue = 0;

        for (int i = 1; i <= iterations; i++) {
            expectedValue += i;
            mockMvc.perform(post(BASE_URL + "/" + TEST_COUNTER_NAME + "/increment")
                            .param("delta", String.valueOf(i)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(expectedValue));
        }

        mockMvc.perform(get(BASE_URL + "/" + TEST_COUNTER_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(55));
    }

    @Test
    @DisplayName("测试负数增量")
    void testNegativeIncrement() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + TEST_COUNTER_NAME + "/increment")
                        .param("delta", "10"))
                .andExpect(status().isOk());

        mockMvc.perform(post(BASE_URL + "/" + TEST_COUNTER_NAME + "/increment")
                        .param("delta", "-3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(7));
    }
}