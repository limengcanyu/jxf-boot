package org.asura.ddd.structure.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asura.ddd.structure.inventory.application.dto.command.InventoryAdjustCommand;
import org.asura.ddd.structure.inventory.application.dto.command.StockAdjustCommand;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String testProductId = "test-product-001";

    @Test
    @Order(1)
    @DisplayName("创建库存 - 真实数据库测试")
    void createInventory_RealDatabase() throws Exception {
        InventoryAdjustCommand command = new InventoryAdjustCommand();
        command.setProductId(testProductId);
        command.setQuantity(50);

        MvcResult result = mockMvc.perform(post("/api/inventories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(testProductId))
                .andExpect(jsonPath("$.quantity").value(50))
                .andExpect(jsonPath("$.reservedQuantity").value(0))
                .andReturn();

        JsonNode responseBody = objectMapper.readTree(result.getResponse().getContentAsString());
        String inventoryId = responseBody.get("id").asText();
        Assertions.assertNotNull(inventoryId, "库存ID不应为空");
    }

    @Test
    @Order(2)
    @DisplayName("查询库存详情 - 真实数据库测试")
    void getInventory_RealDatabase() throws Exception {
        mockMvc.perform(get("/api/inventories/product/{productId}", testProductId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(testProductId))
                .andExpect(jsonPath("$.quantity").value(50));
    }

    @Test
    @Order(3)
    @DisplayName("查询可用库存 - 真实数据库测试")
    void getAvailableStock_RealDatabase() throws Exception {
        mockMvc.perform(get("/api/inventories/product/{productId}/available", testProductId))
                .andExpect(status().isOk())
                .andExpect(content().string("50"));
    }

    @Test
    @Order(4)
    @DisplayName("增加库存 - 真实数据库测试")
    void increaseStock_RealDatabase() throws Exception {
        StockAdjustCommand command = new StockAdjustCommand();
        command.setProductId(testProductId);
        command.setQuantity(30);

        mockMvc.perform(put("/api/inventories/increase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(testProductId))
                .andExpect(jsonPath("$.quantity").value(80));
    }

    @Test
    @Order(5)
    @DisplayName("减少库存 - 真实数据库测试")
    void decreaseStock_RealDatabase() throws Exception {
        StockAdjustCommand command = new StockAdjustCommand();
        command.setProductId(testProductId);
        command.setQuantity(20);

        mockMvc.perform(put("/api/inventories/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(testProductId))
                .andExpect(jsonPath("$.quantity").value(60));
    }

    @Test
    @Order(6)
    @DisplayName("分页查询库存 - 真实数据库测试")
    void queryInventoryPage_RealDatabase() throws Exception {
        mockMvc.perform(get("/api/inventories/page")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.total").isNumber())
                .andExpect(jsonPath("$.pageNum").value(1))
                .andExpect(jsonPath("$.pageSize").value(10));
    }

    @Test
    @Order(7)
    @DisplayName("列表查询库存 - 真实数据库测试")
    void queryInventoryList_RealDatabase() throws Exception {
        mockMvc.perform(get("/api/inventories/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @Order(100)
    @DisplayName("清理测试数据 - 删除测试库存")
    void cleanUpTestData() throws Exception {
        mockMvc.perform(delete("/api/inventories/product/{productId}", testProductId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/inventories/product/{productId}", testProductId))
                .andExpect(status().isBadRequest());
    }
}