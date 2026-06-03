package com.ashi.mockservice.controller;

import com.ashi.mockservice.model.JsonData;
import com.ashi.mockservice.service.JsonDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonControllerUnitTest {

    @Mock
    private JsonDataService jsonDataService;

    @InjectMocks
    private JsonController jsonController;

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
    void testUploadJsonData_Success() {
        when(jsonDataService.uploadJsonData(any(Map.class)))
                .thenReturn(testJsonData);

        ResponseEntity<JsonData> response = jsonController.uploadJsonData(testData);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testId, response.getBody().getId());
        assertEquals("John Doe", response.getBody().getData().get("name"));
    }

    @Test
    void testGetJsonData_Success() {
        when(jsonDataService.getJsonData(eq(testId)))
                .thenReturn(testJsonData);

        ResponseEntity<JsonData> response = jsonController.getJsonData(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testId, response.getBody().getId());
    }

    @Test
    void testGetJsonData_NotFound() {
        when(jsonDataService.getJsonData(eq(testId)))
                .thenThrow(new IllegalArgumentException("JSON data not found with id: " + testId));

        assertThrows(IllegalArgumentException.class, () -> {
            jsonController.getJsonData(testId);
        });
    }

    @Test
    void testUpdateJsonData_Success() {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", "Jane Doe");
        updatedData.put("email", "jane@example.com");

        JsonData updatedJsonData = JsonData.builder()
                .id(testId)
                .data(updatedData)
                .createdAt(testJsonData.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(jsonDataService.updateJsonData(eq(testId), any(Map.class)))
                .thenReturn(updatedJsonData);

        ResponseEntity<JsonData> response = jsonController.updateJsonData(testId, updatedData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jane Doe", response.getBody().getData().get("name"));
    }

    @Test
    void testUpdateJsonData_NotFound() {
        when(jsonDataService.updateJsonData(eq(testId), any(Map.class)))
                .thenThrow(new IllegalArgumentException("JSON data not found with id: " + testId));

        assertThrows(IllegalArgumentException.class, () -> {
            jsonController.updateJsonData(testId, testData);
        });
    }

    @Test
    void testDeleteJsonData_Success() {
        doNothing().when(jsonDataService).deleteJsonData(eq(testId));

        ResponseEntity<Void> response = jsonController.deleteJsonData(testId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(jsonDataService, times(1)).deleteJsonData(eq(testId));
    }

    @Test
    void testDeleteJsonData_NotFound() {
        doThrow(new IllegalArgumentException("JSON data not found with id: " + testId))
                .when(jsonDataService).deleteJsonData(eq(testId));

        assertThrows(IllegalArgumentException.class, () -> {
            jsonController.deleteJsonData(testId);
        });
    }
}

