package org.asura.restful.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asura.restful.dto.request.OrderCreateRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String createdOrderId;

    @Test
    @Order(1)
    void testCreateOrder() throws Exception {
        OrderCreateRequest request = OrderCreateRequest.builder()
                .userId("user-123")
                .totalAmount(new BigDecimal("99.99"))
                .remark("测试订单")
                .build();

        MvcResult result = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data.userId").value("user-123"))
                .andExpect(jsonPath("$.data.totalAmount").value(99.99))
                .andExpect(jsonPath("$.data.status").value(0))
                .andReturn();

        createdOrderId = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("data").get("id").asText();
    }

    @Test
    @Order(2)
    void testGetOrderById() throws Exception {
        mockMvc.perform(get("/orders/{id}", createdOrderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(createdOrderId))
                .andExpect(jsonPath("$.data.orderNo").exists());
    }

    @Test
    @Order(3)
    void testListOrders() throws Exception {
        mockMvc.perform(get("/orders")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.data").isArray())
                .andExpect(jsonPath("$.data.total").isNumber());
    }

    @Test
    @Order(4)
    void testUpdateOrderStatus() throws Exception {
        mockMvc.perform(patch("/orders/{id}/status", createdOrderId)
                        .param("status", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.status").value(1))
                .andExpect(jsonPath("$.data.statusDesc").value("已支付"));
    }

    @Test
    @Order(5)
    void testDeleteOrder() throws Exception {
        mockMvc.perform(delete("/orders/{id}", createdOrderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(204));
    }

    @Test
    @Order(6)
    void testGetOrderByIdNotFound() throws Exception {
        mockMvc.perform(get("/orders/{id}", "nonexistent-id"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }
}