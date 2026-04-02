package com.aditya.infilect.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class ErrorLogService {

    private static final String ERROR_LOG_FILE = "csv_import_errors.log";
    private static final String SUMMARY_LOG_FILE = "csv_import_summary.log";

    public void logRowError(String fileName, int rowNumber, String rowData, Exception error) {
        String errorMessage = String.format("""
            ========================================
            FILE: %s
            ROW NUMBER: %d
            TIMESTAMP: %s
            ROW DATA: %s
            ERROR TYPE: %s
            ERROR MESSAGE: %s
            ========================================
            """,
                fileName,
                rowNumber,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                rowData,
                error.getClass().getSimpleName(),
                error.getMessage()
        );

        // Write to error log file
        try (FileWriter fw = new FileWriter(ERROR_LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.print(errorMessage);
            log.error("Error in row {}: {}", rowNumber, error.getMessage());
        } catch (Exception e) {
            log.error("Failed to write to error log file", e);
        }
    }

    public void logSummary(String fileName, int totalRows, int successCount, int errorCount, long durationMs) {
        String summary = String.format("""
            
            ========================================
            IMPORT SUMMARY
            FILE: %s
            TOTAL ROWS: %d
            SUCCESSFUL: %d
            FAILED: %d
            DURATION: %d ms
            COMPLETION TIME: %s
            ========================================
            """,
                fileName,
                totalRows,
                successCount,
                errorCount,
                durationMs,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        try (FileWriter fw = new FileWriter(SUMMARY_LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.print(summary);
            log.info("Import completed - Success: {}, Failed: {}", successCount, errorCount);
        } catch (Exception e) {
            log.error("Failed to write summary log", e);
        }
    }
}