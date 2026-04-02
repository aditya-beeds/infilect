package com.aditya.infilect.usermasterdb.repo;

import com.aditya.infilect.usermasterdb.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster,Long> {
    Optional<UserMaster> findByUsername(String username);

    Optional<UserMaster> findByEmail(String email);

    Optional<UserMaster> findByUsernameIgnoreCase(String username);

    Optional<UserMaster> findByEmailIgnoreCase(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<UserMaster> findBySupervisorId(Integer supervisorId);

    List<UserMaster> findByUserType(Integer userType);

    List<UserMaster> findByIsActive(Boolean isActive);
}
