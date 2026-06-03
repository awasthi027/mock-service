package com.ashi.mockservice.service;

import com.ashi.mockservice.model.XmlData;
import com.ashi.mockservice.repository.XmlDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class XmlDataServiceTest {

    @Mock
    private XmlDataRepository xmlDataRepository;

    @InjectMocks
    private XmlDataService xmlDataService;

    private UUID testId;
    private String testXmlData;
    private XmlData xmlData;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><name>John Doe</name></root>";
        xmlData = XmlData.builder()
                .id(testId)
                .data(testXmlData)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testUploadXmlData_Success() {
        when(xmlDataRepository.save(any(XmlData.class)))
                .thenReturn(xmlData);

        XmlData result = xmlDataService.uploadXmlData(testXmlData);

        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testXmlData, result.getData());
        verify(xmlDataRepository, times(1)).save(any(XmlData.class));
    }

    @Test
    void testGetXmlData_Success() {
        when(xmlDataRepository.findById(eq(testId)))
                .thenReturn(Optional.of(xmlData));

        XmlData result = xmlDataService.getXmlData(testId);

        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testXmlData, result.getData());
    }

    @Test
    void testGetXmlData_NotFound() {
        when(xmlDataRepository.findById(eq(testId)))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            xmlDataService.getXmlData(testId);
        });
    }

    @Test
    void testUpdateXmlData_Success() {
        String updatedXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><name>Jane Doe</name></root>";

        XmlData updatedXml = XmlData.builder()
                .id(testId)
                .data(updatedXmlData)
                .createdAt(xmlData.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(xmlDataRepository.findById(eq(testId)))
                .thenReturn(Optional.of(xmlData));
        when(xmlDataRepository.save(any(XmlData.class)))
                .thenReturn(updatedXml);

        XmlData result = xmlDataService.updateXmlData(testId, updatedXmlData);

        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(updatedXmlData, result.getData());
        verify(xmlDataRepository, times(1)).save(any(XmlData.class));
    }

    @Test
    void testUpdateXmlData_NotFound() {
        String updatedXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><name>Jane Doe</name></root>";

        when(xmlDataRepository.findById(eq(testId)))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            xmlDataService.updateXmlData(testId, updatedXmlData);
        });
    }

    @Test
    void testDeleteXmlData_Success() {
        when(xmlDataRepository.existsById(eq(testId)))
                .thenReturn(true);

        xmlDataService.deleteXmlData(testId);

        verify(xmlDataRepository, times(1)).deleteById(eq(testId));
    }

    @Test
    void testDeleteXmlData_NotFound() {
        when(xmlDataRepository.existsById(eq(testId)))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            xmlDataService.deleteXmlData(testId);
        });

        verify(xmlDataRepository, never()).deleteById(any());
    }

    @Test
    void testUploadXmlData_EmptyXml() {
        String emptyXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";
        XmlData emptyXmlData = XmlData.builder()
                .id(testId)
                .data(emptyXml)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(xmlDataRepository.save(any(XmlData.class)))
                .thenReturn(emptyXmlData);

        XmlData result = xmlDataService.uploadXmlData(emptyXml);

        assertNotNull(result);
        assertEquals(emptyXml, result.getData());
    }

    @Test
    void testUploadXmlData_ComplexXml() {
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

        when(xmlDataRepository.save(any(XmlData.class)))
                .thenReturn(complexXmlData);

        XmlData result = xmlDataService.uploadXmlData(complexXml);

        assertNotNull(result);
        assertTrue(result.getData().contains("<profile>"));
    }
}

