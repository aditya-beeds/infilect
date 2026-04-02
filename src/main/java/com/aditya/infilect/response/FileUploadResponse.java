package com.aditya.infilect.response;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileUploadResponse {
    private String fileName;
    private String filePath;      // Relative path for static access
    private String absolutePath;  // Absolute path for file operations
    private Long fileSize;
    private String fileType;
    private LocalDateTime uploadTime;
    private String status;
    private String message;

}
