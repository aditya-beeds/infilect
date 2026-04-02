package com.aditya.infilect.storeusermapdb.repo;

import com.aditya.infilect.storeusermapdb.entity.PermanentJourneyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface PermanentJourneyPlanRepository extends JpaRepository<PermanentJourneyPlan, Long> {

    @Modifying
    @Query(value = "REFRESH MATERIALIZED VIEW CONCURRENTLY", nativeQuery = true)
    void forceFlush();
}
