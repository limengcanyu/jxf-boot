package org.asura.restful.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asura.restful.dto.request.UserCreateRequest;
import org.asura.restful.dto.request.UserUpdateRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String createdUserId;

    @Test
    @Order(1)
    void testCreateUser() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .phone("13800138000")
                .password("password123")
                .nickname("测试用户")
                .build();

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andReturn();

        createdUserId = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("data").get("id").asText();
    }

    @Test
    @Order(2)
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/users/{id}", createdUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(createdUserId))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    @Order(3)
    void testListUsers() throws Exception {
        mockMvc.perform(get("/users")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.data").isArray())
                .andExpect(jsonPath("$.data.total").isNumber())
                .andExpect(jsonPath("$.data.pages").isNumber());
    }

    @Test
    @Order(4)
    void testUpdateUser() throws Exception {
        UserUpdateRequest request = UserUpdateRequest.builder()
                .nickname("更新后的昵称")
                .status(1)
                .build();

        mockMvc.perform(put("/users/{id}", createdUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.nickname").value("更新后的昵称"));
    }

    @Test
    @Order(5)
    void testCreateUserWithDuplicateEmail() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
                .username("anotheruser")
                .email("test@example.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("邮箱已被注册"));
    }

    @Test
    @Order(6)
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", createdUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(204));
    }

    @Test
    @Order(7)
    void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/users/{id}", "nonexistent-id"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }
}