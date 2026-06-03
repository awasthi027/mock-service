package com.ashi.mockservice.controller;

import com.ashi.mockservice.model.XmlData;
import com.ashi.mockservice.service.XmlDataService;
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
class XmlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private XmlDataService xmlDataService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testUploadXmlData_Success() throws Exception {
        when(xmlDataService.uploadXmlData(any(String.class)))
                .thenReturn(xmlData);

        mockMvc.perform(post("/api/xml")
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(testXmlData))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.data").value(testXmlData));
    }

    @Test
    void testUploadXmlData_WithComplexXml() throws Exception {
        String complexXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<user>" +
                "<id>123</id>" +
                "<profile>" +
                "<firstName>John</firstName>" +
                "<lastName>Doe</lastName>" +
                "</profile>" +
                "</user>";

        XmlData complexXmlData = XmlData.builder()
                .id(testId)
                .data(complexXml)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(xmlDataService.uploadXmlData(any(String.class)))
                .thenReturn(complexXmlData);

        mockMvc.perform(post("/api/xml")
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(complexXml))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testId.toString()));
    }

    @Test
    void testGetXmlData_Success() throws Exception {
        when(xmlDataService.getXmlData(eq(testId)))
                .thenReturn(xmlData);

        mockMvc.perform(get("/api/xml/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.data").value(testXmlData));
    }

    @Test
    void testGetXmlData_NotFound() throws Exception {
        when(xmlDataService.getXmlData(eq(testId)))
                .thenThrow(new IllegalArgumentException("XML data not found with id: " + testId));

        mockMvc.perform(get("/api/xml/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateXmlData_Success() throws Exception {
        String updatedXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><name>Jane Doe</name><email>jane@example.com</email></root>";

        XmlData updatedXml = XmlData.builder()
                .id(testId)
                .data(updatedXmlData)
                .createdAt(xmlData.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(xmlDataService.updateXmlData(eq(testId), any(String.class)))
                .thenReturn(updatedXml);

        mockMvc.perform(put("/api/xml/{id}", testId)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(updatedXmlData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.data").value(updatedXmlData));
    }

    @Test
    void testUpdateXmlData_NotFound() throws Exception {
        when(xmlDataService.updateXmlData(eq(testId), any(String.class)))
                .thenThrow(new IllegalArgumentException("XML data not found with id: " + testId));

        mockMvc.perform(put("/api/xml/{id}", testId)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(testXmlData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteXmlData_Success() throws Exception {
        doNothing().when(xmlDataService).deleteXmlData(eq(testId));

        mockMvc.perform(delete("/api/xml/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteXmlData_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("XML data not found with id: " + testId))
                .when(xmlDataService).deleteXmlData(eq(testId));

        mockMvc.perform(delete("/api/xml/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUploadXmlData_EmptyXml() throws Exception {
        String emptyXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";

        XmlData emptyXmlData = XmlData.builder()
                .id(testId)
                .data(emptyXml)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(xmlDataService.uploadXmlData(any(String.class)))
                .thenReturn(emptyXmlData);

        mockMvc.perform(post("/api/xml")
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(emptyXml))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(emptyXml));
    }
}

