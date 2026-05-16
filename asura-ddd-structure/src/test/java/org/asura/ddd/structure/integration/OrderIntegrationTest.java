package org.asura.ddd.structure.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asura.ddd.structure.order.application.dto.command.OrderCreateCommand;
import org.asura.ddd.structure.order.application.dto.command.OrderItemDTO;
import org.asura.ddd.structure.order.application.dto.command.ShippingAddressDTO;
import org.asura.ddd.structure.order.application.dto.command.OrderStatusCommand;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String createdOrderId;
    private static final String testUserId = "test-user-123";

    @Test
    @Order(1)
    @DisplayName("创建订单 - 真实数据库测试")
    void createOrder_RealDatabase() throws Exception {
        ShippingAddressDTO shippingAddress = new ShippingAddressDTO();
        shippingAddress.setProvince("广东省");
        shippingAddress.setCity("深圳市");
        shippingAddress.setDistrict("南山区");
        shippingAddress.setDetail("科技园路88号");
        shippingAddress.setZipCode("518000");

        List<OrderItemDTO> items = new ArrayList<>();
        OrderItemDTO item1 = new OrderItemDTO();
        item1.setProductId("p001");
        item1.setProductName("笔记本电脑");
        item1.setUnitPrice(new BigDecimal("2999"));
        item1.setQuantity(1);
        items.add(item1);

        OrderItemDTO item2 = new OrderItemDTO();
        item2.setProductId("p002");
        item2.setProductName("无线鼠标");
        item2.setUnitPrice(new BigDecimal("199"));
        item2.setQuantity(2);
        items.add(item2);

        OrderCreateCommand command = new OrderCreateCommand();
        command.setUserId(testUserId);
        command.setShippingAddress(shippingAddress);
        command.setItems(items);

        MvcResult result = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(testUserId))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].productName").value("笔记本电脑"))
                .andExpect(jsonPath("$.items[1].productName").value("无线鼠标"))
                .andExpect(jsonPath("$.shippingAddress.province").value("广东省"))
                .andExpect(jsonPath("$.shippingAddress.city").value("深圳市"))
                .andReturn();

        JsonNode responseBody = objectMapper.readTree(result.getResponse().getContentAsString());
        createdOrderId = responseBody.get("id").asText();
        Assertions.assertNotNull(createdOrderId, "订单ID不应为空");
    }

    @Test
    @Order(2)
    @DisplayName("查询订单详情 - 真实数据库测试")
    void getOrderById_RealDatabase() throws Exception {
        Assertions.assertNotNull(createdOrderId, "必须先执行创建订单测试");

        mockMvc.perform(get("/api/orders/{orderId}", createdOrderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdOrderId))
                .andExpect(jsonPath("$.userId").value(testUserId))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @Order(3)
    @DisplayName("确认订单 - 真实数据库测试")
    void confirmOrder_RealDatabase() throws Exception {
        Assertions.assertNotNull(createdOrderId, "必须先执行创建订单测试");

        OrderStatusCommand command = new OrderStatusCommand();
        command.setOrderId(createdOrderId);

        mockMvc.perform(put("/api/orders/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdOrderId))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @Order(4)
    @DisplayName("支付订单 - 真实数据库测试")
    void payOrder_RealDatabase() throws Exception {
        Assertions.assertNotNull(createdOrderId, "必须先执行创建订单测试");

        OrderStatusCommand command = new OrderStatusCommand();
        command.setOrderId(createdOrderId);

        mockMvc.perform(put("/api/orders/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdOrderId))
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    @Order(5)
    @DisplayName("发货订单 - 真实数据库测试")
    void shipOrder_RealDatabase() throws Exception {
        Assertions.assertNotNull(createdOrderId, "必须先执行创建订单测试");

        OrderStatusCommand command = new OrderStatusCommand();
        command.setOrderId(createdOrderId);

        mockMvc.perform(put("/api/orders/ship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdOrderId))
                .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    @Test
    @Order(6)
    @DisplayName("完成订单 - 真实数据库测试")
    void completeOrder_RealDatabase() throws Exception {
        Assertions.assertNotNull(createdOrderId, "必须先执行创建订单测试");

        OrderStatusCommand command = new OrderStatusCommand();
        command.setOrderId(createdOrderId);

        mockMvc.perform(put("/api/orders/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdOrderId))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @Order(7)
    @DisplayName("分页查询订单 - 真实数据库测试")
    void queryOrderPage_RealDatabase() throws Exception {
        mockMvc.perform(get("/api/orders/page")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.total").isNumber())
                .andExpect(jsonPath("$.pageNum").value(1))
                .andExpect(jsonPath("$.pageSize").value(10));
    }

    @Test
    @Order(8)
    @DisplayName("列表查询订单 - 真实数据库测试")
    void queryOrderList_RealDatabase() throws Exception {
        mockMvc.perform(get("/api/orders/list")
                        .param("userId", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @Order(9)
    @DisplayName("按用户ID查询订单 - 真实数据库测试")
    void getOrdersByUserId_RealDatabase() throws Exception {
        mockMvc.perform(get("/api/orders/user/{userId}", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].userId").value(testUserId));
    }

    @Test
    @Order(100)
    @DisplayName("清理测试数据 - 删除测试订单")
    void cleanUpTestData() throws Exception {
        if (createdOrderId != null) {
            mockMvc.perform(delete("/api/orders/{orderId}", createdOrderId))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/api/orders/{orderId}", createdOrderId))
                    .andExpect(status().isBadRequest());
        }
    }
}