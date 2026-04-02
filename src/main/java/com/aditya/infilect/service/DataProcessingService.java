package com.aditya.infilect.service;

import com.aditya.infilect.storemasterdb.repo.*;
import com.aditya.infilect.storeusermapdb.entity.PermanentJourneyPlan;
import com.aditya.infilect.storeusermapdb.repo.PermanentJourneyPlanRepository;
import com.aditya.infilect.usermasterdb.entity.UserMaster;
import com.aditya.infilect.usermasterdb.repo.UserMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aditya.infilect.storemasterdb.entity.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class DataProcessingService {

    @Autowired
    private FileService fileService;
    @Autowired
    private UserMasterRepository userRepository;
    @Autowired
    private StoreMasterRepository storeRepository;
    @Autowired
    private PermanentJourneyPlanRepository planRepository;
    @Autowired
    private StoreBrandsRepository storeBrandRepository;
    @Autowired
    private StoreTypesRepository storeTypeRepository;
    @Autowired
    private CitiesRepository cityRepository;
    @Autowired
    private StatesRepository stateRepository;
    @Autowired
    private CountriesRepository countryRepository;
    @Autowired
    private RegionsRepository regionRepository;

    // Track processing status
    private final Map<String, String> processingStatus = new HashMap<>();

    @Transactional
    public void processStoreMaster() throws IOException {
        String absolutePath = fileService.getFilePath("STORE_MASTER");
        if (absolutePath == null) {
            throw new IllegalStateException("Store master file not uploaded yet");
        }

        processingStatus.put("STORE_MASTER", "PROCESSING");
        log.info("Processing store master file from static directory: {}", absolutePath);

        try (BufferedReader br = new BufferedReader(new FileReader(absolutePath))) {
            String line;
            boolean isFirstLine = true;
            int count = 0;
            int errorCount = 0;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    String[] values = line.split(",");
                    if (values.length >= 6) {
                        StoreMaster store = new StoreMaster();
                        store.setStoreId(values[0].trim());
                        store.setStoreExternalId(values[1].trim());
                        store.setName(values[2].trim());
                        store.setTitle(values[3].trim());

                        // Handle brand
                        if (values.length > 4 && !values[4].trim().isEmpty()) {
                            StoreBrands brand = storeBrandRepository.findByNameIgnoreCase(values[4].trim())
                                    .orElseGet(() -> {
                                        StoreBrands newBrand = new StoreBrands();
                                        newBrand.setName(values[4].trim());
                                        return storeBrandRepository.save(newBrand);
                                    });
                            store.setStoreBrands(brand);
                        }

                        // Handle type
                        if (values.length > 5 && !values[5].trim().isEmpty()) {
                            StoreTypes type = storeTypeRepository.findByNameIgnoreCase(values[5].trim())
                                    .orElseGet(() -> {
                                        StoreTypes newType = new StoreTypes();
                                        newType.setName(values[5].trim());
                                        return storeTypeRepository.save(newType);
                                    });
                            store.setStoreType(type);
                        }

                        storeRepository.save(store);
                        count++;
                    }
                } catch (Exception e) {
                    errorCount++;
                    log.error("Error processing line: {}", line, e);
                }

                if (count % 1000 == 0) {
                    log.info("Processed {} store records", count);
                }
            }
            processingStatus.put("STORE_MASTER", "COMPLETED");
            log.info("Completed processing {} store records with {} errors", count, errorCount);
        } catch (Exception e) {
            processingStatus.put("STORE_MASTER", "FAILED");
            throw e;
        }
    }

    @Transactional
    public void processUserMaster() throws IOException {
        String absolutePath = fileService.getFilePath("USER_MASTER");
        if (absolutePath == null) {
            throw new IllegalStateException("User master file not uploaded yet");
        }

        processingStatus.put("USER_MASTER", "PROCESSING");
        log.info("Processing user master file from static directory: {}", absolutePath);

        try (BufferedReader br = new BufferedReader(new FileReader(absolutePath))) {
            String line;
            boolean isFirstLine = true;
            int count = 0;
            int errorCount = 0;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    String[] values = line.split(",");
                    if (values.length >= 5) {
                        UserMaster user = new UserMaster();
                        user.setUsername(values[0].trim());
                        user.setFirstName(values[1].trim());
                        user.setLastName(values[2].trim());
                        user.setEmail(values[3].trim());

                        if (values.length > 4 && !values[4].trim().isEmpty()) {
                            user.setUserType(Integer.parseInt(values[4].trim()));
                        }

                        userRepository.save(user);
                        count++;
                    }
                } catch (Exception e) {
                    errorCount++;
                    log.error("Error processing line: {}", line, e);
                }

                if (count % 1000 == 0) {
                    log.info("Processed {} user records", count);
                }
            }
            processingStatus.put("USER_MASTER", "COMPLETED");
            log.info("Completed processing {} user records with {} errors", count, errorCount);
        } catch (Exception e) {
            processingStatus.put("USER_MASTER", "FAILED");
            throw e;
        }
    }

    @Transactional
    public void processStoreUserMapping() throws IOException {
        String absolutePath = fileService.getFilePath("STORE_USER_MAPPING");
        if (absolutePath == null) {
            throw new IllegalStateException("Store user mapping file not uploaded yet");
        }

        processingStatus.put("STORE_USER_MAPPING", "PROCESSING");
        log.info("Processing store user mapping file from static directory: {}", absolutePath);

        try (BufferedReader br = new BufferedReader(new FileReader(absolutePath))) {
            String line;
            boolean isFirstLine = true;
            int count = 0;
            int errorCount = 0;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    String[] values = line.split("\t");
                    if (values.length >= 4) {
                        // Find user by username
                        UserMaster user = userRepository.findByUsername(values[0].trim())
                                .orElse(null);

                        // Find store by store_id
                        StoreMaster store = storeRepository.findByStoreId(values[1].trim())
                                .orElse(null);

                        if (user != null && store != null) {
                            PermanentJourneyPlan plan = new PermanentJourneyPlan();
                            plan.setUserMaster(user);
                            plan.setStoreMaster(store);
                            plan.setDate(LocalDate.parse(values[2].trim(), dateFormatter));
                            plan.setIsActive(Boolean.parseBoolean(values[3].trim()));

                            planRepository.save(plan);
                            count++;
                        } else {
                            errorCount++;
                            log.warn("User or store not found - User: {}, Store: {}", values[0], values[1]);
                        }
                    }
                } catch (Exception e) {
                    errorCount++;
                    log.error("Error processing line: {}", line, e);
                }

                if (count % 1000 == 0) {
                    log.info("Processed {} mapping records", count);
                }
            }
            processingStatus.put("STORE_USER_MAPPING", "COMPLETED");
            log.info("Completed processing {} mapping records with {} errors", count, errorCount);
        } catch (Exception e) {
            processingStatus.put("STORE_USER_MAPPING", "FAILED");
            throw e;
        }
    }

    // For large 500K file - will be used later
    public void processLargeStoreFile() throws IOException {
        String absolutePath = fileService.getFilePath("STORES_MASTER_500K");
        if (absolutePath == null) {
            throw new IllegalStateException("Large store master file (500K) not uploaded yet");
        }

        log.info("Large file ready for processing from static directory: {}", absolutePath);
        log.info("File size: {} bytes", new java.io.File(absolutePath).length());
        processingStatus.put("STORES_MASTER_500K", "READY");
        // This method will be implemented later for large file processing
    }

    public boolean isLargeFileReady() {
        return fileService.isFileUploaded("STORES_MASTER_500K");
    }

    public Map<String, String> getProcessingStatus() {
        return new HashMap<>(processingStatus);
    }

}
