package com.app.first_arrival.repository;

import com.app.first_arrival.entities.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    Optional<Certification> findByCertificationCode(String certificationCode);

    boolean existsByCertificationCode(String certificationCode);
}
