package com.aditya.infilect.usermasterdb.repo;

import com.aditya.infilect.usermasterdb.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
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


    @Query("SELECT u FROM UserMaster u WHERE LOWER(u.username) = LOWER(:username) OR " +
            "LOWER(SUBSTRING(u.email, 1, LOCATE('@', u.email) - 1)) = LOWER(:username)")
    Optional<UserMaster> findByUsernameOrEmailPrefix(@Param("username") String username);

}
