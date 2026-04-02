package com.aditya.infilect.controller;

import com.aditya.infilect.response.FileUploadResponse;
import com.aditya.infilect.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadingFileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/store-master")
    public ResponseEntity<FileUploadResponse> uploadStoreMaster(@RequestParam("file") MultipartFile file) {
        try {
            validateFile(file, "store_master.csv");
            FileUploadResponse response = fileService.saveFile(file, "STORE_MASTER");
            log.info("Store master file saved to static directory");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Failed to upload store master file", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/stores-master-500k")
    public ResponseEntity<FileUploadResponse> uploadStoresMaster500K(@RequestParam("file") MultipartFile file) {
        try {
            validateFile(file, "stores_master_500K.csv");
            log.info("Receiving large file of size: {} MB", file.getSize() / (1024 * 1024));

            FileUploadResponse response = fileService.saveFile(file, "STORES_MASTER_500K");
            response.setMessage("Large file (68MB) uploaded successfully to static directory. Ready for processing when triggered.");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Failed to upload large stores master file", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/store-user-mapping")
    public ResponseEntity<FileUploadResponse> uploadStoreUserMapping(@RequestParam("file") MultipartFile file) {
        try {
            validateFile(file, "store_user_mapping.csv");
            FileUploadResponse response = fileService.saveFile(file, "STORE_USER_MAPPING");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Failed to upload store user mapping file", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/user-master")
    public ResponseEntity<FileUploadResponse> uploadUserMaster(@RequestParam("file") MultipartFile file) {
        try {
            validateFile(file, "user_master.csv");
            FileUploadResponse response = fileService.saveFile(file, "USER_MASTER");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Failed to upload user master file", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getUploadStatus() {
        return ResponseEntity.ok(fileService.getAllUploadedFiles());
    }

    @GetMapping("/large-file-ready")
    public ResponseEntity<Boolean> isLargeFileReady() {
        return ResponseEntity.ok(fileService.isFileUploaded("STORES_MASTER_500K"));
    }

    private void validateFile(MultipartFile file, String expectedName) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (!file.getOriginalFilename().endsWith(".csv")) {
            throw new IllegalArgumentException("Only CSV files are allowed");
        }

        log.info("Received file: {}, size: {} bytes", file.getOriginalFilename(), file.getSize());
    }


}
