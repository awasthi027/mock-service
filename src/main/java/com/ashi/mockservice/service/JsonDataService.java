package com.ashi.mockservice.service;

import com.ashi.mockservice.model.JsonData;
import com.ashi.mockservice.repository.JsonDataRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class JsonDataService {

    private final JsonDataRepository jsonDataRepository;

    public JsonDataService(JsonDataRepository jsonDataRepository) {
        this.jsonDataRepository = jsonDataRepository;
    }

    /**
     * Upload JSON data
     */
    public JsonData uploadJsonData(Map<String, Object> data) {
        JsonData jsonData = JsonData.builder()
                .data(data)
                .build();
        return jsonDataRepository.save(jsonData);
    }

    /**
     * Get JSON data by ID
     */
    public JsonData getJsonData(UUID id) {
        return jsonDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("JSON data not found with id: " + id));
    }

    /**
     * Update JSON data
     */
    public JsonData updateJsonData(UUID id, Map<String, Object> data) {
        JsonData jsonData = getJsonData(id);
        jsonData.setData(data);
        return jsonDataRepository.save(jsonData);
    }

    /**
     * Delete JSON data
     */
    public void deleteJsonData(UUID id) {
        if (!jsonDataRepository.existsById(id)) {
            throw new IllegalArgumentException("JSON data not found with id: " + id);
        }
        jsonDataRepository.deleteById(id);
    }
}

