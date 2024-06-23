package com.app.first_arrival.entities.dto;

import com.app.first_arrival.entities.enums.EmergencyStatus;
import com.app.first_arrival.entities.enums.Severity;
import javax.persistence.ManyToMany;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmergencyDTO implements Serializable {

    private Long id;

    private Integer locationId;

    private Long reportedById;

    private EmergencyStatus status;

    private LocalDateTime timestamp;

    private String emergencyType;

    @Nullable
    private Severity severity;

    @Nullable
    private String nrOfVictims;

    @Nullable
    private String symptomsDescription;

    private List<String> medicalHistory;

    private List<String> medications;

    @Nullable
    private String additionalInformation;

    private List<Long> volunteersAcceptedIds;
}
