package com.aditya.infilect.service;

import com.aditya.infilect.config.FileStorageConfig;
import com.aditya.infilect.response.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class FileService {

    @Autowired
    private FileStorageConfig fileStorageConfig;

    // Map to track uploaded files
    private final Map<String, String> uploadedFiles = new HashMap<>();

    public FileUploadResponse saveFile(MultipartFile file, String category) throws IOException {
        Path uploadPath = fileStorageConfig.getUploadPath();

        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String originalFileName = file.getOriginalFilename();
        String fileName = category + "_" + originalFileName;

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        String relativePath = "/uploads/" + fileName;
        uploadedFiles.put(category, relativePath);

        log.info("File saved to static directory: {}", filePath.toString());
        log.info("Relative path: {}", relativePath);

        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(originalFileName);
        response.setFilePath(relativePath);
        response.setAbsolutePath(filePath.toString());
        response.setFileSize(file.getSize());
        response.setFileType(file.getContentType());
        response.setUploadTime(LocalDateTime.now());
        response.setStatus("UPLOADED");
        response.setMessage("File uploaded successfully to static directory");
        return response;
    }

    public String getFilePath(String category) {
        String relativePath = uploadedFiles.get(category);
        if (relativePath != null) {
            try {
                Path uploadPath = fileStorageConfig.getUploadPath();
                String fileName = relativePath.substring(relativePath.lastIndexOf("/") + 1);
                return uploadPath.resolve(fileName).toString();
            } catch (Exception e) {
                log.error("Error getting absolute path", e);
                return null;
            }
        }
        return null;
    }

    public String getRelativePath(String category) {
        return uploadedFiles.get(category);
    }

    public boolean isFileUploaded(String category) {
        return uploadedFiles.containsKey(category);
    }

    public Map<String, String> getAllUploadedFiles() {
        return new HashMap<>(uploadedFiles);
    }

    public void clearUploadedFile(String category) {
        uploadedFiles.remove(category);
    }
}
