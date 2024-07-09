package com.app.first_arrival.entities.dto;

import lombok.Data;
import java.util.List;

@Data
public class EmergencyVolunteerStatistics {
    private int emergenciesWithVolunteers;
    private double averageVolunteersPerEmergency;
    private List<TopVolunteer> topVolunteers;
}
