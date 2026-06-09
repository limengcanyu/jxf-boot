package org.asura.ai.controller;

import org.asura.ai.entity.Document;
import org.asura.ai.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
public class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Test
    public void testUploadDocument() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Hello World".getBytes()
        );

        Document mockDocument = new Document();
        mockDocument.setId("test-id");
        mockDocument.setFilename("test.txt");
        when(documentService.uploadDocument(file)).thenReturn(mockDocument);

        mockMvc.perform(multipart("/api/documents/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-id"))
                .andExpect(jsonPath("$.filename").value("test.txt"));
    }

    @Test
    public void testGetAllDocuments() throws Exception {
        Document mockDocument = new Document();
        mockDocument.setId("test-id");
        mockDocument.setFilename("test.txt");
        when(documentService.getAllDocuments()).thenReturn(List.of(mockDocument));

        mockMvc.perform(get("/api/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("test-id"));
    }

    @Test
    public void testGetDocumentById() throws Exception {
        Document mockDocument = new Document();
        mockDocument.setId("test-id");
        mockDocument.setFilename("test.txt");
        when(documentService.getDocumentById("test-id")).thenReturn(mockDocument);

        mockMvc.perform(get("/api/documents/test-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-id"));
    }

    @Test
    public void testGetDocumentByIdNotFound() throws Exception {
        when(documentService.getDocumentById("not-exist-id")).thenReturn(null);

        mockMvc.perform(get("/api/documents/not-exist-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteDocument() throws Exception {
        mockMvc.perform(delete("/api/documents/test-id"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testSearchDocuments() throws Exception {
        Document mockDocument = new Document();
        mockDocument.setId("test-id");
        mockDocument.setFilename("test.txt");
        when(documentService.searchDocuments("test")).thenReturn(List.of(mockDocument));

        mockMvc.perform(post("/api/documents/search")
                        .contentType("application/json")
                        .content("{\"keyword\":\"test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("test-id"));
    }
}