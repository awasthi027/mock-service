package com.ashi.mockservice.controller;

import com.ashi.mockservice.model.XmlData;
import com.ashi.mockservice.service.XmlDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/xml")
public class XmlController {

    private final XmlDataService xmlDataService;

    public XmlController(XmlDataService xmlDataService) {
        this.xmlDataService = xmlDataService;
    }

    /**
     * Upload XML data
     * POST /api/xml
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<XmlData> uploadXmlData(@RequestBody String data) {
        XmlData result = xmlDataService.uploadXmlData(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Get XML data by ID
     * GET /api/xml/{id}
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<XmlData> getXmlData(@PathVariable UUID id) {
        XmlData result = xmlDataService.getXmlData(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Update XML data
     * PUT /api/xml/{id}
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<XmlData> updateXmlData(
            @PathVariable UUID id,
            @RequestBody String data) {
        XmlData result = xmlDataService.updateXmlData(id, data);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete XML data
     * DELETE /api/xml/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteXmlData(@PathVariable UUID id) {
        xmlDataService.deleteXmlData(id);
        return ResponseEntity.noContent().build();
    }
}

