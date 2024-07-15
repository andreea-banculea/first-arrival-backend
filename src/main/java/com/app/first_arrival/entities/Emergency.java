package com.app.first_arrival.entities;

import com.app.first_arrival.entities.enums.EmergencyStatus;
import com.app.first_arrival.entities.enums.Severity;

import javax.persistence.*;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table
public class Emergency implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Location location;

    @ManyToOne
    private User reportedBy;

    @Enumerated(EnumType.STRING)
    private EmergencyStatus status;

    private LocalDateTime timestamp;

    private String emergencyType;

    @Nullable
    private Severity severity;

    @Nullable
    private String nrOfVictims;

    @Nullable
    private String symptomsDescription;

    @ManyToMany
    private List<MedicalCondition> medicalHistory;

    @ManyToMany
    private List<Medication> medications;

    private String additionalInformation;

    @OneToMany
    private List<User> volunteersAccepted;
}