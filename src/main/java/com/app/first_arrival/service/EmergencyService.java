package com.app.first_arrival.service;

import com.app.first_arrival.entities.Emergency;
import com.app.first_arrival.entities.MedicalCondition;
import com.app.first_arrival.entities.Medication;
import com.app.first_arrival.entities.dto.EmergencyDTO;
import com.app.first_arrival.entities.enums.EmergencyStatus;
import com.app.first_arrival.entities.enums.Role;
import com.app.first_arrival.entities.User;
import com.app.first_arrival.mapper.EmergencyMapper;
import com.app.first_arrival.repository.EmergencyRepository;
import com.app.first_arrival.repository.MedicalConditionRepository;
import com.app.first_arrival.repository.MedicationRepository;
import com.app.first_arrival.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class EmergencyService {

    private final EmergencyRepository emergencyRepository;

    private final MedicalConditionRepository medicalConditionRepository;

    private final MedicationRepository medicationRepository;

    private final UserRepository userRepository;

    private final LocationService locationService;

    private final NotificationService notificationService;

    private final EmergencyMapper emergencyMapper;

    @Autowired
    public EmergencyService(EmergencyRepository emergencyRepository, MedicalConditionRepository medicalConditionRepository, MedicationRepository medicationRepository, UserRepository userRepository, LocationService locationService, NotificationService notificationService, EmergencyMapper emergencyMapper) {
        this.emergencyRepository = emergencyRepository;
        this.medicalConditionRepository = medicalConditionRepository;
        this.medicationRepository = medicationRepository;
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.notificationService = notificationService;
        this.emergencyMapper = emergencyMapper;

    }

    public List<Emergency> findAll() {
        return emergencyRepository.findAll();
    }

    public Optional<Emergency> findById(Long id) {
        return emergencyRepository.findById(id);

    }

    public Emergency save(Emergency emergency) {
        return emergencyRepository.save(emergency);
    }

    public EmergencyDTO update(EmergencyDTO emergencyDTO) {
        Optional<Emergency> existingEmergency = emergencyRepository.findById(emergencyDTO.getId());

        if (existingEmergency.isPresent()) {
            saveMedicalConditions(emergencyDTO.getMedicalHistory());
            saveMedications(emergencyDTO.getMedications());
            Emergency emergency = emergencyMapper.toEntity(emergencyDTO);
            emergency.setId(emergencyDTO.getId());
            return emergencyMapper.toDTO(emergencyRepository.save(emergency));
        } else {
            throw new IllegalArgumentException("Emergency not found");
        }
    }

    private void saveMedications(List<String> medications) {
        medications.forEach(name -> {
            if (!medicationRepository.existsByName(name)) {
                Medication medication = new Medication();
                medication.setName(name);
                medicationRepository.save(medication);
            }
        });
    }

    private void saveMedicalConditions(List<String> medicalHistory) {
        medicalHistory.forEach(name -> {
            if (!medicalConditionRepository.existsByName(name)) {
                MedicalCondition medicalCondition = new MedicalCondition();
                medicalCondition.setName(name);
                medicalConditionRepository.save(medicalCondition);
            }
        });
    }

    public void cancelEmergencyById(Long id) {
        Emergency emergency = emergencyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Emergency not found"));
        emergency.setStatus(EmergencyStatus.CANCELLED);
        emergencyRepository.save(emergency);
    }

    public void resolveEmergencyById(Long id) {
        Emergency emergency = emergencyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Emergency not found"));
        emergency.setStatus(EmergencyStatus.RESOLVED);
        emergencyRepository.save(emergency);
    }

    public List<Emergency> findAllEmergenciesWithStatusActive() {
        return emergencyRepository.findAllByStatusIn(
                List.of(EmergencyStatus.PENDING, EmergencyStatus.ACCEPTED));
    }

    public Emergency acceptEmergency(Long emergencyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getPrincipal().toString());
        if (user.isPresent()) {
            Optional<Emergency> emergency = emergencyRepository.findById(emergencyId);
            if (emergency.isPresent()) {
                if (emergency.get().getVolunteersAccepted().contains(user.get())) {
                    throw new IllegalArgumentException("Volunteer is already assigned to this emergency");
                }
                emergency.get().getVolunteersAccepted().add(user.get());
                emergency.get().setStatus(EmergencyStatus.ACCEPTED);
                return emergencyRepository.save(emergency.get());
            } else {
                throw new EntityNotFoundException("Emergency not found");
            }
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    public Emergency abortEmergency(Long emergencyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getPrincipal().toString());
        if (user.isPresent()) {
            Optional<Emergency> emergency = emergencyRepository.findById(emergencyId);
            if (emergency.isPresent()) {
                emergency.get().getVolunteersAccepted().remove(user.get());
                if (emergency.get().getVolunteersAccepted().size() == 0)
                    emergency.get().setStatus(EmergencyStatus.PENDING);
                return emergencyRepository.save(emergency.get());
            } else {
                throw new EntityNotFoundException("Emergency not found");
            }
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    public Emergency hasAnActiveEmergencyReported() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getPrincipal().toString());
        if (user.isPresent()) {
            Optional<Emergency> emergency = emergencyRepository.findByReportedBy_IdAndStatusIn(user.get().getId(), List.of(EmergencyStatus.PENDING, EmergencyStatus.ACCEPTED));
            return emergency.orElse(null);
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    public void notifyNearbyUsers(Emergency emergency) {
        double emergencyLat = emergency.getLocation().getLatitude();
        double emergencyLon = emergency.getLocation().getLongitude();

        List<User> allUsers = userRepository.findAllByRole(Role.VOLUNTEER);
        List<User> nearbyUsers = locationService.getUsersWithinRadius(emergencyLat, emergencyLon, 1.0, allUsers);

        for (User user : nearbyUsers) {
            if (user.getId().equals(emergency.getReportedBy().getId())) {
                continue;
            }
            double distance = locationService.calculateDistance(emergencyLat, emergencyLon, user.getLocation().getLatitude(), user.getLocation().getLongitude());
            notificationService.sendNotification(user, emergency, distance);
        }
    }


    public void deleteEmergencyById(Long id) {
        if(emergencyRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Emergency not found");
        }
        emergencyRepository.deleteById(id);
    }
}
