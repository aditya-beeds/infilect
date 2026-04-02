package com.aditya.infilect.storemasterdb.repo;

import com.aditya.infilect.storemasterdb.entity.StoreMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreMasterRepository extends JpaRepository<StoreMaster,Long> {

}
