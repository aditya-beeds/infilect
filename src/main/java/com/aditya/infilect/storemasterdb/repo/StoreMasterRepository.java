package com.aditya.infilect.storemasterdb.repo;

import com.aditya.infilect.storemasterdb.entity.StoreMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreMasterRepository extends JpaRepository<StoreMaster,Long> {

    Optional<StoreMaster> findByStoreId(String trim);
    boolean existsByStoreId(String trim);


}
