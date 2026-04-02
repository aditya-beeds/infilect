package com.aditya.infilect.storemasterdb.repo;

import com.aditya.infilect.storemasterdb.entity.StoreMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

@Repository
public interface StoreMasterRepository extends JpaRepository<StoreMaster,Long> {

    Optional<StoreMaster> findByStoreId(String trim);
    boolean existsByStoreId(String trim);


    @Modifying
    @Query(value = "REFRESH MATERIALIZED VIEW CONCURRENTLY", nativeQuery = true)
    void forceFlush();

}
