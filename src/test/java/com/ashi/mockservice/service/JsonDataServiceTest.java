package com.ashi.mockservice.service;

import com.ashi.mockservice.model.JsonData;
import com.ashi.mockservice.repository.JsonDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonDataServiceTest {

    @Mock
    private JsonDataRepository jsonDataRepository;

    @InjectMocks
    private JsonDataService jsonDataService;

    private UUID testId;
    private Map<String, Object> testData;
    private JsonData testJsonData;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testData = new HashMap<>();
        testData.put("name", "John Doe");
        testData.put("email", "john@example.com");

        testJsonData = JsonData.builder()
                .id(testId)
                .data(testData)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testUploadJsonData_Success() {
        when(jsonDataRepository.save(any(JsonData.class)))
                .thenReturn(testJsonData);

        JsonData result = jsonDataService.uploadJsonData(testData);

        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testData, result.getData());
        verify(jsonDataRepository, times(1)).save(any(JsonData.class));
    }

    @Test
    void testGetJsonData_Success() {
        when(jsonDataRepository.findById(eq(testId)))
                .thenReturn(Optional.of(testJsonData));

        JsonData result = jsonDataService.getJsonData(testId);

        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testData, result.getData());
    }

    @Test
    void testGetJsonData_NotFound() {
        when(jsonDataRepository.findById(eq(testId)))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            jsonDataService.getJsonData(testId);
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

        when(jsonDataRepository.findById(eq(testId)))
                .thenReturn(Optional.of(testJsonData));
        when(jsonDataRepository.save(any(JsonData.class)))
                .thenReturn(updatedJsonData);

        JsonData result = jsonDataService.updateJsonData(testId, updatedData);

        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(updatedData, result.getData());
        verify(jsonDataRepository, times(1)).save(any(JsonData.class));
    }

    @Test
    void testUpdateJsonData_NotFound() {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", "Jane Doe");

        when(jsonDataRepository.findById(eq(testId)))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            jsonDataService.updateJsonData(testId, updatedData);
        });
    }

    @Test
    void testDeleteJsonData_Success() {
        when(jsonDataRepository.existsById(eq(testId)))
                .thenReturn(true);

        jsonDataService.deleteJsonData(testId);

        verify(jsonDataRepository, times(1)).deleteById(eq(testId));
    }

    @Test
    void testDeleteJsonData_NotFound() {
        when(jsonDataRepository.existsById(eq(testId)))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            jsonDataService.deleteJsonData(testId);
        });

        verify(jsonDataRepository, never()).deleteById(any());
    }

    @Test
    void testUploadJsonData_EmptyData() {
        Map<String, Object> emptyData = new HashMap<>();
        JsonData emptyJsonData = JsonData.builder()
                .id(testId)
                .data(emptyData)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(jsonDataRepository.save(any(JsonData.class)))
                .thenReturn(emptyJsonData);

        JsonData result = jsonDataService.uploadJsonData(emptyData);

        assertNotNull(result);
        assertTrue(result.getData().isEmpty());
    }

    @Test
    void testUploadJsonData_ComplexData() {
        Map<String, Object> complexData = new HashMap<>();
        complexData.put("user", new HashMap<String, String>() {{
            put("firstName", "John");
            put("lastName", "Doe");
        }});
        complexData.put("age", 30);
        complexData.put("active", true);

        JsonData complexJsonData = JsonData.builder()
                .id(testId)
                .data(complexData)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(jsonDataRepository.save(any(JsonData.class)))
                .thenReturn(complexJsonData);

        JsonData result = jsonDataService.uploadJsonData(complexData);

        assertNotNull(result);
        assertEquals(3, result.getData().size());
        assertTrue(result.getData().containsKey("user"));
    }
}

