package com.aditya.infilect.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileStorageConfig {

    @Value("${file.upload-dir:static/uploads}")
    private String uploadDir;

    @Getter
    private Path uploadPath;

    @PostConstruct
    public void init() throws IOException {
        // Get the classpath root
        ClassPathResource resource = new ClassPathResource("");
        Path classpathRoot = Paths.get(resource.getURI());

        // Create uploads directory inside static
        uploadPath = classpathRoot.resolve(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        System.out.println("Upload directory created at: " + uploadPath.toString());
    }

}
