package com.ashi.mockservice.controller;

import com.ashi.mockservice.model.XmlData;
import com.ashi.mockservice.service.XmlDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class XmlControllerUnitTest {

    @Mock
    private XmlDataService xmlDataService;

    @InjectMocks
    private XmlController xmlController;

    private UUID testId;
    private String testXmlData;
    private XmlData xmlData;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><name>John Doe</name><email>john@example.com</email></root>";
        xmlData = XmlData.builder()
                .id(testId)
                .data(testXmlData)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testUploadXmlData_Success() {
        when(xmlDataService.uploadXmlData(any(String.class)))
                .thenReturn(xmlData);

        ResponseEntity<XmlData> response = xmlController.uploadXmlData(testXmlData);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testId, response.getBody().getId());
        assertTrue(response.getBody().getData().contains("<name>John Doe</name>"));
    }

    @Test
    void testGetXmlData_Success() {
        when(xmlDataService.getXmlData(eq(testId)))
                .thenReturn(xmlData);

        ResponseEntity<XmlData> response = xmlController.getXmlData(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testId, response.getBody().getId());
    }

    @Test
    void testGetXmlData_NotFound() {
        when(xmlDataService.getXmlData(eq(testId)))
                .thenThrow(new IllegalArgumentException("XML data not found with id: " + testId));

        assertThrows(IllegalArgumentException.class, () -> {
            xmlController.getXmlData(testId);
        });
    }

    @Test
    void testUpdateXmlData_Success() {
        String updatedXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><name>Jane Doe</name><email>jane@example.com</email></root>";

        XmlData updatedXml = XmlData.builder()
                .id(testId)
                .data(updatedXmlData)
                .createdAt(xmlData.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(xmlDataService.updateXmlData(eq(testId), any(String.class)))
                .thenReturn(updatedXml);

        ResponseEntity<XmlData> response = xmlController.updateXmlData(testId, updatedXmlData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getData().contains("<name>Jane Doe</name>"));
    }

    @Test
    void testUpdateXmlData_NotFound() {
        when(xmlDataService.updateXmlData(eq(testId), any(String.class)))
                .thenThrow(new IllegalArgumentException("XML data not found with id: " + testId));

        assertThrows(IllegalArgumentException.class, () -> {
            xmlController.updateXmlData(testId, testXmlData);
        });
    }

    @Test
    void testDeleteXmlData_Success() {
        doNothing().when(xmlDataService).deleteXmlData(eq(testId));

        ResponseEntity<Void> response = xmlController.deleteXmlData(testId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(xmlDataService, times(1)).deleteXmlData(eq(testId));
    }

    @Test
    void testDeleteXmlData_NotFound() {
        doThrow(new IllegalArgumentException("XML data not found with id: " + testId))
                .when(xmlDataService).deleteXmlData(eq(testId));

        assertThrows(IllegalArgumentException.class, () -> {
            xmlController.deleteXmlData(testId);
        });
    }
}

