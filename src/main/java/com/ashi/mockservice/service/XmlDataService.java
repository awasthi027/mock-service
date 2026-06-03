package com.ashi.mockservice.service;

import com.ashi.mockservice.model.XmlData;
import com.ashi.mockservice.repository.XmlDataRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class XmlDataService {

    private final XmlDataRepository xmlDataRepository;

    public XmlDataService(XmlDataRepository xmlDataRepository) {
        this.xmlDataRepository = xmlDataRepository;
    }

    /**
     * Upload XML data
     */
    public XmlData uploadXmlData(String data) {
        XmlData xmlData = XmlData.builder()
                .data(data)
                .build();
        return xmlDataRepository.save(xmlData);
    }

    /**
     * Get XML data by ID
     */
    public XmlData getXmlData(UUID id) {
        return xmlDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("XML data not found with id: " + id));
    }

    /**
     * Update XML data
     */
    public XmlData updateXmlData(UUID id, String data) {
        XmlData xmlData = getXmlData(id);
        xmlData.setData(data);
        return xmlDataRepository.save(xmlData);
    }

    /**
     * Delete XML data
     */
    public void deleteXmlData(UUID id) {
        if (!xmlDataRepository.existsById(id)) {
            throw new IllegalArgumentException("XML data not found with id: " + id);
        }
        xmlDataRepository.deleteById(id);
    }
}

