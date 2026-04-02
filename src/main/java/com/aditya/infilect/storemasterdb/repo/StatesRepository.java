package com.aditya.infilect.storemasterdb.repo;

import com.aditya.infilect.storemasterdb.entity.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface StatesRepository extends JpaRepository<States,Long> {
    Optional<States> findByNameIgnoreCase(String name);
}
