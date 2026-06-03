package com.ashi.mockservice.controller;

import com.ashi.mockservice.model.JsonData;
import com.ashi.mockservice.service.JsonDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class JsonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JsonDataService jsonDataService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID testId;
    private Map<String, Object> testData;
    private JsonData testJsonData;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testData = new HashMap<>();
        testData.put("name", "John Doe");
        testData.put("email", "john@example.com");
        testData.put("age", 30);

        testJsonData = JsonData.builder()
                .id(testId)
                .data(testData)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testUploadJsonData_Success() throws Exception {
        when(jsonDataService.uploadJsonData(any(Map.class)))
                .thenReturn(testJsonData);

        mockMvc.perform(post("/api/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    void testUploadJsonData_EmptyData() throws Exception {
        Map<String, Object> emptyData = new HashMap<>();
        when(jsonDataService.uploadJsonData(any(Map.class)))
                .thenReturn(JsonData.builder()
                        .id(testId)
                        .data(emptyData)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());

        mockMvc.perform(post("/api/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyData)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetJsonData_Success() throws Exception {
        when(jsonDataService.getJsonData(eq(testId)))
                .thenReturn(testJsonData);

        mockMvc.perform(get("/api/json/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    void testGetJsonData_NotFound() throws Exception {
        when(jsonDataService.getJsonData(eq(testId)))
                .thenThrow(new IllegalArgumentException("JSON data not found with id: " + testId));

        mockMvc.perform(get("/api/json/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateJsonData_Success() throws Exception {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", "Jane Doe");
        updatedData.put("email", "jane@example.com");
        updatedData.put("age", 28);

        JsonData updatedJsonData = JsonData.builder()
                .id(testId)
                .data(updatedData)
                .createdAt(testJsonData.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(jsonDataService.updateJsonData(eq(testId), any(Map.class)))
                .thenReturn(updatedJsonData);

        mockMvc.perform(put("/api/json/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.data.name").value("Jane Doe"))
                .andExpect(jsonPath("$.data.age").value(28));
    }

    @Test
    void testUpdateJsonData_NotFound() throws Exception {
        when(jsonDataService.updateJsonData(eq(testId), any(Map.class)))
                .thenThrow(new IllegalArgumentException("JSON data not found with id: " + testId));

        mockMvc.perform(put("/api/json/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteJsonData_Success() throws Exception {
        doNothing().when(jsonDataService).deleteJsonData(eq(testId));

        mockMvc.perform(delete("/api/json/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteJsonData_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("JSON data not found with id: " + testId))
                .when(jsonDataService).deleteJsonData(eq(testId));

        mockMvc.perform(delete("/api/json/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUploadJsonData_ComplexData() throws Exception {
        Map<String, Object> complexData = new HashMap<>();
        complexData.put("user", new HashMap<String, String>() {{
            put("firstName", "John");
            put("lastName", "Doe");
        }});
        complexData.put("active", true);

        JsonData complexJsonData = JsonData.builder()
                .id(testId)
                .data(complexData)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(jsonDataService.uploadJsonData(any(Map.class)))
                .thenReturn(complexJsonData);

        mockMvc.perform(post("/api/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(complexData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.active").value(true));
    }
}

