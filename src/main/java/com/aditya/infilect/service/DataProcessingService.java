package com.aditya.infilect.service;

import com.aditya.infilect.dto.PermanentJourneyPlanDTO;
import com.aditya.infilect.dto.StoreMasterDTO;
import com.aditya.infilect.dto.UserMasterDTO;
import com.aditya.infilect.storemasterdb.repo.*;
import com.aditya.infilect.storeusermapdb.entity.PermanentJourneyPlan;
import com.aditya.infilect.usermasterdb.entity.UserMaster;
import com.opencsv.CSVReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.aditya.infilect.storemasterdb.entity.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class DataProcessingService {

    @Autowired
    private FileService fileService;
    @Autowired
    private StoreMasterService storeMasterService;
    @Autowired
    private UserMasterService userMasterService;
    @Autowired
    private PermanentJourneyPlanService permanentJourneyPlanService;
    @Autowired
    private ErrorLogService errorLogService;
    @PersistenceContext
    private EntityManager entityManager;

    private final Map<String, String> processingStatus = new HashMap<>();


    @Async("taskExecutor")
    public void processStoreMaster() throws IOException {
        String absolutePath = fileService.getFilePath("STORE_MASTER");
        if (absolutePath == null) throw new IllegalStateException("STORE master file not uploaded yet");
        processingStatus.put("STORE_MASTER", "PROCESSING");
        log.info("Processing STORE master file from static directory: {}", absolutePath);

        Path csvPath = Paths.get(absolutePath);
        int successCount = 0;
        int errorCount = 0;
        int rowNumber = 0;

        log.info("Starting Store Master import from: {}", "STORE_MASTER");
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(csvPath, StandardCharsets.UTF_8))) {
            reader.readNext();
            String[] row;
            long startTime = System.currentTimeMillis();
            while ((row = reader.readNext()) != null) {
                rowNumber++;
                try {
                    StoreMasterDTO storeMasterDTO = new StoreMasterDTO(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], row[8], row[9], (row[10]), (row[11]));
                    StoreMaster store = this.storeMasterService.createStore(storeMasterDTO);
                    log.debug("Saved row {}: {}", rowNumber, store.getStoreId());
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                    errorLogService.logRowError(absolutePath, rowNumber, Arrays.toString(row), e);
                }
                entityManager.clear();
                if (rowNumber % 1000 == 0)
                    log.info("StoreMasterData.csv-Processed {} rows, Success: {}, Failed: {}", rowNumber, successCount, errorCount);
            }
            long duration = System.currentTimeMillis() - startTime;
            errorLogService.logSummary(absolutePath, rowNumber, successCount, errorCount, duration);
            log.info("Import completed for StoreMasterData.csv - Total: {}, Success: {}, Failed: {}", rowNumber, successCount, errorCount);
        } catch (Exception e) {
            log.error("Import failed", e);
        }
    }


    @Async("taskExecutor")
    public void processUserMaster() throws IOException {
        String absolutePath = fileService.getFilePath("USER_MASTER");
        if (absolutePath == null) throw new IllegalStateException("USER master file not uploaded yet");
        processingStatus.put("USER_MASTER", "PROCESSING");
        log.info("Processing USER master file from static directory: {}", absolutePath);

        Path csvPath = Paths.get(absolutePath);
        int successCount = 0;
        int errorCount = 0;
        int rowNumber = 0;

        log.info("Starting USER Master import from: {}", "USER_MASTER");
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(csvPath, StandardCharsets.UTF_8))) {
            reader.readNext();
            String[] row;
            long startTime = System.currentTimeMillis();
            while ((row = reader.readNext()) != null) {
                rowNumber++;
                try {
                    UserMasterDTO userMasterDTO = new UserMasterDTO(row[0], row[1], row[2], row[3], Integer.parseInt(row[4]), Long.parseLong(row[5]), row[6], row[7]);
                    UserMaster user = this.userMasterService.createUser(userMasterDTO);
                    log.debug("USER-MASTER-Saved row {}: {}", rowNumber, user.getUsername());
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                    errorLogService.logRowError(absolutePath, rowNumber, Arrays.toString(row), e);
                }
                entityManager.clear();
                if (rowNumber % 1000 == 0)
                    log.info("USER-MASTER-Processed {} rows, Success: {}, Failed: {}", rowNumber, successCount, errorCount);
            }
            long duration = System.currentTimeMillis() - startTime;
            errorLogService.logSummary(absolutePath, rowNumber, successCount, errorCount, duration);
            log.info("USER-MASTER Import completed - Total: {}, Success: {}, Failed: {}", rowNumber, successCount, errorCount);
        } catch (Exception e) {
            log.error("USER-MASTER Import failed", e);
        }
    }

    @Async("taskExecutor")
    public void processStoreUserMapping() throws IOException {
        String absolutePath = fileService.getFilePath("STORE_USER_MAPPING");
        if (absolutePath == null) throw new IllegalStateException("STORE_USER_MAPPING file not uploaded yet");
        processingStatus.put("STORE_USER_MAPPING", "PROCESSING");
        log.info("Processing STORE_USER_MAPPING  file from static directory: {}", absolutePath);

        Path csvPath = Paths.get(absolutePath);
        int successCount = 0;
        int errorCount = 0;
        int rowNumber = 0;

        log.info("Starting STORE_USER_MAPPING import from: {}", "STORE_USER_MAPPING");
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(csvPath, StandardCharsets.UTF_8))) {
            reader.readNext();
            String[] row;
            long startTime = System.currentTimeMillis();
            while ((row = reader.readNext()) != null) {
                rowNumber++;
                try {
                    PermanentJourneyPlanDTO permanentJourneyPlanDTO = new PermanentJourneyPlanDTO(row[0], row[1], row[2], row[3]);
                    PermanentJourneyPlan plan = this.permanentJourneyPlanService.createPlan(permanentJourneyPlanDTO);
                    log.debug("STORE_USER_MAPPING-Saved row {}: {}:{}:{}", rowNumber, plan.getUserMaster().getUsername(), plan.getStoreMaster().getStoreId(), plan.getDate());
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                    errorLogService.logRowError(absolutePath, rowNumber, Arrays.toString(row), e);
                }
                entityManager.clear();
                if (rowNumber % 1000 == 0)
                    log.info("STORE_USER_MAPPING-Processed {} rows, Success: {}, Failed: {}", rowNumber, successCount, errorCount);
            }
            long duration = System.currentTimeMillis() - startTime;
            errorLogService.logSummary(absolutePath, rowNumber, successCount, errorCount, duration);
            log.info("STORE_USER_MAPPING Import completed - Total: {}, Success: {}, Failed: {}", rowNumber, successCount, errorCount);
        } catch (Exception e) {
            log.error("STORE_USER_MAPPING Import failed", e);
        }
    }

    public boolean isLargeFileReady() {
        return fileService.isFileUploaded("STORES_MASTER_500K");
    }

    public Map<String, String> getProcessingStatus() {
        return new HashMap<>(processingStatus);
    }

}
