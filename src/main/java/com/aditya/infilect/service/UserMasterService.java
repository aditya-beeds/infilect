package com.aditya.infilect.service;

import com.aditya.infilect.dto.UserMasterDTO;
import com.aditya.infilect.storemasterdb.entity.StoreBrands;
import com.aditya.infilect.usermasterdb.entity.UserMaster;
import com.aditya.infilect.usermasterdb.repo.UserMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserMasterService {

    @Autowired
    private UserMasterRepository userRepository;


    @Transactional
    public UserMaster createUser(UserMasterDTO userMasterDTO) {

        UserMaster userMaster = new UserMaster(
                null,
                userMasterDTO.getUsername(),
                userMasterDTO.getFirstName(),
                userMasterDTO.getLastName(),
                userMasterDTO.getEmail(),
                userMasterDTO.getUserType(),
                userMasterDTO.getPhoneNumber().toString(),
                null,
                userMasterDTO.getIsActive().equalsIgnoreCase("true"),
                null,
                null
                );
        Optional<UserMaster> byUsername = this.userRepository.findByUsername(userMasterDTO.getSupervisorUsername());
        if (userMasterDTO.getSupervisorUsername() != null && !userMasterDTO.getSupervisorUsername().trim().isEmpty()) {
            String supervisorName = userMasterDTO.getSupervisorUsername().trim();
            UserMaster userMasterOfSuperVisor = userRepository.findByUsernameIgnoreCase(supervisorName)
                    .orElse(null);
            userMaster.setSupervisor(userMasterOfSuperVisor);
        }
        return userRepository.save(userMaster);
    }

}
