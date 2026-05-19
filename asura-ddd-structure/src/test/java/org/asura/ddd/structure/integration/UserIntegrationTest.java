package org.asura.ddd.structure.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asura.ddd.structure.user.application.dto.command.AddressDTO;
import org.asura.ddd.structure.user.application.dto.command.UserRegisterCommand;
import org.asura.ddd.structure.user.domain.repository.UserRepository;
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
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String createdUserId;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    @DisplayName("注册用户 - 真实数据库测试")
    void registerUser_RealDatabase() throws Exception {
        AddressDTO address = new AddressDTO();
        address.setProvince("广东省");
        address.setCity("广州市");
        address.setDistrict("天河区");
        address.setDetail("珠江新城路100号");
        address.setZipCode("510000");

        UserRegisterCommand command = new UserRegisterCommand();
        command.setUsername("integration_test_user");
        command.setEmail("integration_test@example.com");
        command.setPhoneNumber("13900139000");
        command.setAddress(address);

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("integration_test_user"))
                .andExpect(jsonPath("$.email").value("integration_test@example.com"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.address.province").value("广东省"))
                .andExpect(jsonPath("$.address.city").value("广州市"))
                .andReturn();

        JsonNode responseBody = objectMapper.readTree(result.getResponse().getContentAsString());
        createdUserId = responseBody.get("id").asText();
        Assertions.assertNotNull(createdUserId, "用户ID不应为空");
    }

    @Test
    @Order(2)
    @DisplayName("查询用户详情 - 真实数据库测试")
    void getUserById_RealDatabase() throws Exception {
        Assertions.assertNotNull(createdUserId, "必须先执行注册测试");

        mockMvc.perform(get("/api/users/{id}", createdUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUserId))
                .andExpect(jsonPath("$.username").value("integration_test_user"))
                .andExpect(jsonPath("$.email").value("integration_test@example.com"));
    }

    @Test
    @Order(3)
    @DisplayName("更新用户信息 - 真实数据库测试")
    void updateUser_RealDatabase() throws Exception {
        Assertions.assertNotNull(createdUserId, "必须先执行注册测试");

        String updateJson = "{\"email\":\"updated_test@example.com\",\"phoneNumber\":\"13900139001\"}";

        mockMvc.perform(put("/api/users/{id}", createdUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUserId))
                .andExpect(jsonPath("$.email").value("updated_test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("+8613900139001"));
    }

    @Test
    @Order(4)
    @DisplayName("禁用用户 - 真实数据库测试")
    void disableUser_RealDatabase() throws Exception {
        Assertions.assertNotNull(createdUserId, "必须先执行注册测试");

        mockMvc.perform(put("/api/users/{id}/disable", createdUserId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/{id}", createdUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false));
    }

    @Test
    @Order(5)
    @DisplayName("启用用户 - 真实数据库测试")
    void enableUser_RealDatabase() throws Exception {
        Assertions.assertNotNull(createdUserId, "必须先执行注册测试");

        mockMvc.perform(put("/api/users/{id}/enable", createdUserId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/{id}", createdUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    @Order(6)
    @DisplayName("分页查询用户 - 真实数据库测试")
    void queryUserPage_RealDatabase() throws Exception {
        mockMvc.perform(get("/api/users/page")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.total").isNumber())
                .andExpect(jsonPath("$.pageNum").value(1))
                .andExpect(jsonPath("$.pageSize").value(10));
    }

    @Test
    @Order(7)
    @DisplayName("列表查询用户 - 真实数据库测试")
    void queryUserList_RealDatabase() throws Exception {
        mockMvc.perform(get("/api/users/list")
                        .param("username", "integration_test_user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].username").value("integration_test_user"));
    }

    @Test
    @Order(100)
    @DisplayName("清理测试数据 - 删除测试用户")
    void cleanUpTestData() throws Exception {
        if (createdUserId != null) {
            mockMvc.perform(delete("/api/users/{id}", createdUserId))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/api/users/{id}", createdUserId))
                    .andExpect(status().isBadRequest());
        }
    }
}