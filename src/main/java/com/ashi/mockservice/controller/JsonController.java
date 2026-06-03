package com.ashi.mockservice.controller;

import com.ashi.mockservice.model.JsonData;
import com.ashi.mockservice.service.JsonDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/json")
public class JsonController {

    private final JsonDataService jsonDataService;

    public JsonController(JsonDataService jsonDataService) {
        this.jsonDataService = jsonDataService;
    }

    /**
     * Upload JSON data
     * POST /api/json
     */
    @PostMapping
    public ResponseEntity<JsonData> uploadJsonData(@RequestBody Map<String, Object> data) {
        JsonData result = jsonDataService.uploadJsonData(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Get JSON data by ID
     * GET /api/json/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<JsonData> getJsonData(@PathVariable UUID id) {
        JsonData result = jsonDataService.getJsonData(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Update JSON data
     * PUT /api/json/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<JsonData> updateJsonData(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> data) {
        JsonData result = jsonDataService.updateJsonData(id, data);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete JSON data
     * DELETE /api/json/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJsonData(@PathVariable UUID id) {
        jsonDataService.deleteJsonData(id);
        return ResponseEntity.noContent().build();
    }
}

