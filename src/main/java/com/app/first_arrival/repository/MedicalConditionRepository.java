package com.app.first_arrival.repository;

import com.app.first_arrival.entities.MedicalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalConditionRepository extends JpaRepository<MedicalCondition, Integer> {
    Optional<MedicalCondition> findByName(String s);

    boolean existsByName(String name);
}
