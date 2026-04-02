package com.aditya.infilect.storemasterdb.repo;

import com.aditya.infilect.storemasterdb.entity.Regions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionsRepository extends JpaRepository<Regions,Long> {
    Optional<Regions> findByNameIgnoreCase(String name);
}
