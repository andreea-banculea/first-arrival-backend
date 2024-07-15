package com.app.first_arrival.service;

import com.app.first_arrival.entities.Certification;
import com.app.first_arrival.entities.dto.VolunteerRequest;
import com.app.first_arrival.entities.dto.VolunteerRequestDTO;
import com.app.first_arrival.entities.enums.CertificationStatus;
import com.app.first_arrival.entities.enums.Role;
import com.app.first_arrival.entities.User;
import com.app.first_arrival.repository.CertificationRepository;
import com.app.first_arrival.repository.EmergencyRepository;
import com.app.first_arrival.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final CertificationRepository certificationRepository;

    private final EmergencyRepository emergencyRepository;

    @Autowired
    public UserService(UserRepository userRepository, CertificationRepository certificationRepository, EmergencyRepository emergencyRepository) {
        this.userRepository = userRepository;
        this.certificationRepository = certificationRepository;
        this.emergencyRepository = emergencyRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        emergencyRepository.findAllByReportedBy_Id(id).forEach(emergency -> {
            emergency.setReportedBy(null);
            emergencyRepository.save(emergency);
        });
        userRepository.deleteById(id);
    }

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    public User createUser(User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder().encode(user.getPassword()));
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setLocation(user.getLocation());
        newUser.setRole(user.getRole());
        newUser.setCertification(user.getCertification());
        newUser.setVolunteerLevel(user.getVolunteerLevel());

        return userRepository.save(newUser);
    }

    public User getLoggedInUser(String principal) {
        return userRepository.findByEmail(principal).orElse(null);
    }

    @Transactional
    public Optional<User> updateUser(User user) {
        return userRepository.findById(user.getId()).map(existingUser -> {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder().encode(user.getPassword()));
            }
            existingUser.setLocation(user.getLocation());
            existingUser.setRole(user.getRole());
            existingUser.setCertification(user.getCertification());
            existingUser.setVolunteerLevel(user.getVolunteerLevel());
            return userRepository.save(existingUser);
        });
    }

    public void saveUserToken(Long userId, String token) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPushToken(token);
        userRepository.save(user);
    }

    public void volunteerRequest(VolunteerRequest volunteerRequest) {
        userRepository.findById(volunteerRequest.getUserId()).map(existingUser -> {
            if(!certificationRepository.existsByCertificationCode(volunteerRequest.getCertificationCode())) {
                Certification certification = new Certification(volunteerRequest.getCertificationCode(), CertificationStatus.PENDING);
                existingUser.setCertification(certificationRepository.save(certification));
            }
            return userRepository.save(existingUser);
        });
    }

    public void acceptVolunteerRequest(Long userId) {
        userRepository.findById(userId).ifPresent(existingUser -> {
            if (existingUser.getCertification() != null) {
                Optional<Certification> certification = certificationRepository.findByCertificationCode(existingUser.getCertification().getCertificationCode());
                if(certification.isPresent()) {
                    certification.get().setStatus(CertificationStatus.ACCEPTED);
                    certificationRepository.save(certification.get());
                }
                existingUser.setRole(Role.VOLUNTEER);
                userRepository.save(existingUser);
            }
        });
    }

    public void declineVolunteerRequest(Long userId) {
        userRepository.findById(userId).map(existingUser -> {
            if (existingUser.getCertification() != null) {
                Optional<Certification> certification = certificationRepository.findByCertificationCode(existingUser.getCertification().getCertificationCode());
                if(certification.isPresent()) {
                    certification.get().setStatus(CertificationStatus.DECLINED);
                    certificationRepository.save(certification.get());
                }
                existingUser.setCertification(null);
            }
            return userRepository.save(existingUser);
        });
    }

    public List<VolunteerRequestDTO> getAllVolunteerRequests() {
        return userRepository.findAllByCertification_Status(CertificationStatus.PENDING).stream().map(user -> {
            if (user.getCertification() != null) {
                return new VolunteerRequestDTO(user.getName(), user.getCertification().getCertificationCode(), user.getId());
            }
            return null;
        }).toList();
    }
}
