package com.aditya.infilect.service;

import com.aditya.infilect.dto.PermanentJourneyPlanDTO;
import com.aditya.infilect.service.dto.PermanentJourneyPlanDTO;
import com.aditya.infilect.storemasterdb.entity.StoreMaster;
import com.aditya.infilect.storemasterdb.repo.StoreMasterRepository;
import com.aditya.infilect.storeusermapdb.entity.PermanentJourneyPlan;
import com.aditya.infilect.storeusermapdb.repo.PermanentJourneyPlanRepository;
import com.aditya.infilect.usermasterdb.entity.UserMaster;
import com.aditya.infilect.usermasterdb.repo.UserMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PermanentJourneyPlanService {

    @Autowired
    private  PermanentJourneyPlanRepository planRepository;
    @Autowired
    private  UserMasterRepository userRepository;
    @Autowired
    private  StoreMasterRepository storeRepository;


    @Transactional
    public PermanentJourneyPlan createPlan(PermanentJourneyPlanDTO plan) {


        // Verify user exists (if using Integer IDs)
        Optional<UserMaster> byUsername = userRepository.findByUsername(plan.getUsername());
        if (byUsername.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + plan.getUsername());
        }

        // Verify store exists
        Optional<StoreMaster> byStoreId = storeRepository.findByStoreId(plan.getStore_id());
        if (byStoreId.isEmpty()) {
            throw new IllegalArgumentException("Store not found with id: " + plan.getStore_id());
        }


        DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("M/dd/yyyy");
        LocalDate date = LocalDate.parse(plan.getDate(), formatter4);
        PermanentJourneyPlan permanentJourneyPlan = new PermanentJourneyPlan(null, byUsername.get(), byStoreId.get(), date, plan.getIs_active(), null, null);
        return planRepository.save(permanentJourneyPlan);
    }



}
