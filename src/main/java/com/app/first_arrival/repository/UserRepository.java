package com.app.first_arrival.repository;

import com.app.first_arrival.entities.enums.CertificationStatus;
import com.app.first_arrival.entities.enums.Role;
import com.app.first_arrival.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findAllByCertification_Status(CertificationStatus status);

    List<User> findAllByRole(Role role);
}
