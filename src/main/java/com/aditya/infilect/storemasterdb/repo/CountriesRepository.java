package com.aditya.infilect.storemasterdb.repo;


import com.aditya.infilect.storemasterdb.entity.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountriesRepository  extends JpaRepository<Countries, Long> {
    Optional<Countries> findByNameIgnoreCase(String name);
}
