package com.app.first_arrival.repository;

import com.app.first_arrival.entities.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    Optional<Medication> findByName(String s);

    boolean existsByName(String name);
}
