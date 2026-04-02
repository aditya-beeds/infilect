package com.aditya.infilect.controller;

import com.aditya.infilect.response.FileUploadResponse;
import com.aditya.infilect.service.DataProcessingService;
import com.aditya.infilect.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/process")
@Slf4j
public class DataProcessingController {

    @Autowired
    private DataProcessingService dataProcessingService;

    @PostMapping("/store-master")
    public ResponseEntity<Map<String, String>> processStoreMaster() {
        try {
            dataProcessingService.processStoreMaster();
            return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "Store master processed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "ERROR", "message", e.getMessage()));
        }
    }

    @PostMapping("/user-master")
    public ResponseEntity<Map<String, String>> processUserMaster() {
        try {
            dataProcessingService.processUserMaster();
            return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "User master processed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "ERROR", "message", e.getMessage()));
        }
    }

    @PostMapping("/store-user-mapping")
    public ResponseEntity<Map<String, String>> processStoreUserMapping() {
        try {
            dataProcessingService.processStoreUserMapping();
            return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "Store user mapping processed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "ERROR", "message", e.getMessage()));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getProcessingStatus() {
        return ResponseEntity.ok(dataProcessingService.getProcessingStatus());
    }
}
