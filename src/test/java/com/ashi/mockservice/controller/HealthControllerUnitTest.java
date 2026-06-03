package com.ashi.mockservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for HealthController
 */
@SpringBootTest
@AutoConfigureMockMvc
class HealthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Mock Service"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testHealthDetailedEndpoint() throws Exception {
        mockMvc.perform(get("/api/health/detailed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Mock Service"))
                .andExpect(jsonPath("$.version").value("0.0.1-SNAPSHOT"))
                .andExpect(jsonPath("$.components.database").value("UP"))
                .andExpect(jsonPath("$.components.json-endpoints").value("UP"))
                .andExpect(jsonPath("$.components.xml-endpoints").value("UP"));
    }

    @Test
    void testReadyEndpoint() throws Exception {
        mockMvc.perform(get("/api/health/ready"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ready").value("true"))
                .andExpect(jsonPath("$.service").value("Mock Service"));
    }

    @Test
    void testLiveEndpoint() throws Exception {
        mockMvc.perform(get("/api/health/live"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alive").value("true"))
                .andExpect(jsonPath("$.service").value("Mock Service"));
    }

    @Test
    void testInfoEndpoint() throws Exception {
        mockMvc.perform(get("/api/health/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application").value("Mock Service"))
                .andExpect(jsonPath("$.version").value("0.0.1"))
                .andExpect(jsonPath("$.endpoints").isArray())
                .andExpect(jsonPath("$.endpoints.length()").value(13));
    }
}

