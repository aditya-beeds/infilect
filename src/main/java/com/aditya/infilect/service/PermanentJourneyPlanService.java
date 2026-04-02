package com.aditya.infilect.service;

import com.aditya.infilect.service.dto.PermanentJourneyPlanDTO;
import com.aditya.infilect.storemasterdb.repo.StoreMasterRepository;
import com.aditya.infilect.storeusermapdb.entity.PermanentJourneyPlan;
import com.aditya.infilect.storeusermapdb.repo.PermanentJourneyPlanRepository;
import com.aditya.infilect.usermasterdb.repo.UserMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (!userRepository.existsById(plan.getUser)) {
            throw new IllegalArgumentException("User not found with id: " + plan.getUserId());
        }

        // Verify store exists
        if (!storeRepository.existsById(plan.getStoreId())) {
            throw new IllegalArgumentException("Store not found with id: " + plan.getStoreId());
        }

        // Check if unique constraint would be violated
        if (planRepository.findByUserIdAndStoreIdAndDate(
                plan.getUserId(), plan.getStoreId(), plan.getDate()).isPresent()) {
            throw new IllegalArgumentException(
                    "Journey plan already exists for this user, store, and date");
        }

        return planRepository.save(plan);
    }



}
