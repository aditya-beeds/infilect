package com.aditya.infilect.storemasterdb.repo;

import com.aditya.infilect.storemasterdb.entity.StoreBrands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface StoreBrandsRepository extends JpaRepository<StoreBrands,Long> {
    Optional<StoreBrands> findByNameIgnoreCase(String name);
}
