package com.aditya.infilect.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileStorageConfig {

    @Value("${file.upload-dir:classpath:static/uploads}")
    private String uploadDir;

    @Getter
    private Path uploadPath;

    @PostConstruct
    public void init() throws IOException {
        // Get static directory path
        File staticDir = ResourceUtils.getFile("classpath:static");
        if (!staticDir.exists()) {
            boolean mkdir = staticDir.mkdir();
        }

        // Create uploads directory inside static
        uploadPath = Paths.get(staticDir.getAbsolutePath(), "uploads");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        System.out.println("Upload directory created at: " + uploadPath.toString());
    }

}
