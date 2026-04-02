package com.aditya.infilect.storeusermapdb.repo;

import com.aditya.infilect.storeusermapdb.entity.PermanentJourneyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PermanentJourneyPlanRepository extends JpaRepository<PermanentJourneyPlan, Long> {

    @Modifying
    @Query(value = "REFRESH MATERIALIZED VIEW CONCURRENTLY", nativeQuery = true)
    void forceFlush();

    List<PermanentJourneyPlan> findByUserId(Integer userId);

    List<PermanentJourneyPlan> findByStoreId(Integer storeId);

    List<PermanentJourneyPlan> findByDate(LocalDate date);

    List<PermanentJourneyPlan> findByUserIdAndStoreId(Integer userId, Integer storeId);

    Optional<PermanentJourneyPlan> findByUserIdAndStoreIdAndDate(Integer userId, Integer storeId, LocalDate date);

    List<PermanentJourneyPlan> findByIsActiveTrue();

    List<PermanentJourneyPlan> findByUserIdAndIsActiveTrue(Integer userId);

    List<PermanentJourneyPlan> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
