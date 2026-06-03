package com.ashi.mockservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * Provides endpoints for monitoring service health and readiness
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    /**
     * Simple health check endpoint - returns 200 OK if service is running
     * @return Health status
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Mock Service");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }

    /**
     * Detailed health check endpoint - returns comprehensive service information
     * @return Detailed health status
     */
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> healthDetailed() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Mock Service");
        response.put("version", "0.0.1-SNAPSHOT");
        response.put("timestamp", System.currentTimeMillis());

        Map<String, String> components = new HashMap<>();
        components.put("database", "UP");
        components.put("json-endpoints", "UP");
        components.put("xml-endpoints", "UP");
        response.put("components", components);

        return ResponseEntity.ok(response);
    }

    /**
     * Readiness probe endpoint - used for Kubernetes/Docker health checks
     * @return Readiness status
     */
    @GetMapping("/ready")
    public ResponseEntity<Map<String, String>> ready() {
        Map<String, String> response = new HashMap<>();
        response.put("ready", "true");
        response.put("service", "Mock Service");
        return ResponseEntity.ok(response);
    }

    /**
     * Liveness probe endpoint - used for Kubernetes/Docker health checks
     * @return Liveness status
     */
    @GetMapping("/live")
    public ResponseEntity<Map<String, String>> live() {
        Map<String, String> response = new HashMap<>();
        response.put("alive", "true");
        response.put("service", "Mock Service");
        return ResponseEntity.ok(response);
    }

    /**
     * Root endpoint with service information
     * @return Service info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Mock Service");
        response.put("description", "A service for uploading, updating, and deleting JSON and XML data");
        response.put("version", "0.0.1");
        response.put("endpoints", new String[]{
            "POST /api/json - Upload JSON data",
            "GET /api/json/{id} - Retrieve JSON data",
            "PUT /api/json/{id} - Update JSON data",
            "DELETE /api/json/{id} - Delete JSON data",
            "POST /api/xml - Upload XML data",
            "GET /api/xml/{id} - Retrieve XML data",
            "PUT /api/xml/{id} - Update XML data",
            "DELETE /api/xml/{id} - Delete XML data",
            "GET /api/health - Health check",
            "GET /api/health/detailed - Detailed health status",
            "GET /api/health/ready - Readiness probe",
            "GET /api/health/live - Liveness probe",
            "GET /api/health/info - Service information"
        });
        return ResponseEntity.ok(response);
    }
}

