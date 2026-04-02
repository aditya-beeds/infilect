package com.aditya.infilect.storemasterdb.repo;

import com.aditya.infilect.storemasterdb.entity.Regions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Repository
public interface RegionsRepository extends JpaRepository<Regions,Long> {
    Optional<Regions> findByNameIgnoreCase(String name);


    @Modifying
    @Query(value = "REFRESH MATERIALIZED VIEW CONCURRENTLY", nativeQuery = true)
    void forceFlush();
}
