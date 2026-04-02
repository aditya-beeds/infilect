package com.aditya.infilect.usermasterdb.repo;

import com.aditya.infilect.usermasterdb.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster,Long> {

    @Modifying
    @Query(value = "REFRESH MATERIALIZED VIEW CONCURRENTLY", nativeQuery = true)
    void forceFlush();


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
