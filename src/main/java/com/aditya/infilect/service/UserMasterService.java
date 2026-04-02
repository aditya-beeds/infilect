package com.aditya.infilect.service;

import com.aditya.infilect.usermasterdb.entity.UserMaster;
import com.aditya.infilect.usermasterdb.repo.UserMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMasterService {

    @Autowired
    private UserMasterRepository userRepository;


    @Transactional
    public UserMaster createUser(UserMaster user) {
        // Validate user type
        return userRepository.save(user);
    }

}
