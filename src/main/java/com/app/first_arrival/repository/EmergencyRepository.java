package com.app.first_arrival.repository;

import com.app.first_arrival.entities.Emergency;
import com.app.first_arrival.entities.enums.EmergencyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyRepository extends JpaRepository<Emergency, Long> {
    List<Emergency> findAllByStatusIn(List<EmergencyStatus> statuses);

    Optional<Emergency> findByReportedBy_IdAndStatusIn(Long userId, List<EmergencyStatus> statuses);
}
