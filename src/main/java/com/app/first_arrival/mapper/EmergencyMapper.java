package com.app.first_arrival.mapper;

import com.app.first_arrival.entities.Emergency;
import com.app.first_arrival.entities.Location;
import com.app.first_arrival.entities.MedicalCondition;
import com.app.first_arrival.entities.Medication;
import com.app.first_arrival.entities.dto.EmergencyDTO;
import com.app.first_arrival.entities.User;
import com.app.first_arrival.repository.LocationRepository;
import com.app.first_arrival.repository.MedicalConditionRepository;
import com.app.first_arrival.repository.MedicationRepository;
import com.app.first_arrival.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EmergencyMapper {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MedicalConditionRepository medicalConditionRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private UserRepository userRepository;

    public EmergencyDTO toDTO(Emergency emergency) {
        if (emergency == null) {
            return null;
        }

        EmergencyDTO dto = new EmergencyDTO();
        dto.setId(emergency.getId());
        dto.setLocationId(emergency.getLocation() != null ? emergency.getLocation().getId() : null);
        dto.setReportedById(emergency.getReportedBy() != null ? emergency.getReportedBy().getId() : null);
        dto.setStatus(emergency.getStatus());
        dto.setTimestamp(emergency.getTimestamp());
        dto.setEmergencyType(emergency.getEmergencyType());
        dto.setSeverity(emergency.getSeverity());
        dto.setNrOfVictims(emergency.getNrOfVictims());
        dto.setSymptomsDescription(emergency.getSymptomsDescription());
        dto.setMedicalHistory(emergency.getMedicalHistory() != null ?
                emergency.getMedicalHistory().stream().map(MedicalCondition::getName).collect(Collectors.toList()) : null);
        dto.setMedications(emergency.getMedications() != null ?
                emergency.getMedications().stream().map(Medication::getName).collect(Collectors.toList()) : null);
        dto.setAdditionalInformation(emergency.getAdditionalInformation());
        dto.setVolunteersAcceptedIds(emergency.getVolunteersAccepted() != null ?
                emergency.getVolunteersAccepted().stream().map(User::getId).collect(Collectors.toList()) : null);

        return dto;
    }

    public Emergency toEntity(EmergencyDTO dto) {
        if (dto == null) {
            return null;
        }

        Emergency emergency = new Emergency();
        emergency.setId(dto.getId());

        if (dto.getLocationId() != null) {
            Optional<Location> location = locationRepository.findById(dto.getLocationId());
            location.ifPresent(emergency::setLocation);
        }

        if(dto.getReportedById() != null) {
            Optional<User> reportedBy = userRepository.findById(dto.getReportedById());
            reportedBy.ifPresent(emergency::setReportedBy);
        }

        if (dto.getMedicalHistory() != null) {
            List<MedicalCondition> medicalHistory = dto.getMedicalHistory().stream()
                    .map(medicalConditionRepository::findByName)
                    .map(opt -> opt.orElse(null))
                    .collect(Collectors.toList());
            emergency.setMedicalHistory(medicalHistory);
        }

        if (dto.getMedications() != null) {
            List<Medication> medications = dto.getMedications().stream()
                    .map(medicationRepository::findByName)
                    .map(opt -> opt.orElse(null))
                    .collect(Collectors.toList());
            emergency.setMedications(medications);
        }

        if (dto.getVolunteersAcceptedIds() != null) {
            List<User> volunteers = dto.getVolunteersAcceptedIds().stream()
                    .map(userRepository::findById)
                    .map(opt -> opt.orElse(null))
                    .collect(Collectors.toList());
            emergency.setVolunteersAccepted(volunteers);
        }

        emergency.setStatus(dto.getStatus());
        emergency.setTimestamp(dto.getTimestamp());
        emergency.setEmergencyType(dto.getEmergencyType());
        emergency.setSeverity(dto.getSeverity());
        emergency.setNrOfVictims(dto.getNrOfVictims());
        emergency.setSymptomsDescription(dto.getSymptomsDescription());
        emergency.setAdditionalInformation(dto.getAdditionalInformation());

        return emergency;
    }
}
