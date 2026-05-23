package org.asura.ai.agent.controller;

import org.asura.ai.agent.service.RAGService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RAGController.class)
public class RAGControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RAGService ragService;

    @Test
    public void testAskWithoutConversationId() throws Exception {
        when(ragService.ask("Hello")).thenReturn("Hello! This is a test response.");

        mockMvc.perform(post("/api/rag/ask")
                        .contentType("application/json")
                        .content("{\"question\":\"Hello\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Hello! This is a test response."));
    }

    @Test
    public void testAskWithConversationId() throws Exception {
        when(ragService.askWithContext("Hello", "test-conversation")).thenReturn("Hello with context!");

        mockMvc.perform(post("/api/rag/ask")
                        .contentType("application/json")
                        .content("{\"question\":\"Hello\",\"conversationId\":\"test-conversation\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Hello with context!"))
                .andExpect(jsonPath("$.conversationId").value("test-conversation"));
    }

    @Test
    public void testClearConversation() throws Exception {
        mockMvc.perform(delete("/api/rag/conversation/test-conversation"))
                .andExpect(status().isNoContent());
    }
}