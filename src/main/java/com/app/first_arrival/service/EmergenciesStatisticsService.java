package com.app.first_arrival.service;

import com.app.first_arrival.entities.Emergency;
import com.app.first_arrival.entities.dto.Dataset;
import com.app.first_arrival.entities.dto.EmergencyStatistics;
import com.app.first_arrival.entities.dto.EmergencyVolunteerStatistics;
import com.app.first_arrival.entities.dto.TopVolunteer;
import com.app.first_arrival.entities.enums.EmergencyStatus;
import com.app.first_arrival.entities.enums.EmergencyTypeEnum;
import com.app.first_arrival.entities.enums.Severity;
import com.app.first_arrival.entities.users.User;
import com.app.first_arrival.repository.EmergencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmergenciesStatisticsService {

    private final EmergencyRepository emergencyRepository;

    @Autowired
    public EmergenciesStatisticsService(EmergencyRepository emergencyRepository) {
        this.emergencyRepository = emergencyRepository;
    }

    public Integer getEmergencyCount() {
        return Math.toIntExact(emergencyRepository.count());
    }

    public EmergencyStatistics getEmergencyTypeStatistics() {
        List<Emergency> emergencies = emergencyRepository.findAll();

        // Initialize the map with all possible emergency types and set the count to 0
        Map<EmergencyTypeEnum, Integer> emergencyTypeCount = new EnumMap<>(EmergencyTypeEnum.class);
        for (EmergencyTypeEnum type : EmergencyTypeEnum.values()) {
            emergencyTypeCount.put(type, 0);
        }

        // Count occurrences of each emergency type
        for (Emergency emergency : emergencies) {
            String typeString = emergency.getEmergencyType();
            if (typeString != null) {
                try {
                    EmergencyTypeEnum type = EmergencyTypeEnum.valueOf(typeString.toUpperCase().replace(" ", "_"));
                    emergencyTypeCount.put(type, emergencyTypeCount.get(type) + 1);
                } catch (IllegalArgumentException e) {
                    // Handle case where the string does not match any enum constant
                    System.err.println("Unknown emergency type: " + typeString);
                }
            }
        }

        // Prepare the data for the DTO
        List<String> emergencyTypes = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        List<String> backgroundColor = generateColors(emergencyTypeCount.size());

        for (Map.Entry<EmergencyTypeEnum, Integer> entry : emergencyTypeCount.entrySet()) {
            emergencyTypes.add(entry.getKey().getDisplayName());  // Add display name
            data.add(entry.getValue());
        }

        Dataset dataset = new Dataset();
        dataset.setLabel("Emergency Types");
        dataset.setData(data);
        dataset.setBackgroundColor(backgroundColor);

        EmergencyStatistics statistics = new EmergencyStatistics();
        statistics.setLabels(emergencyTypes);
        statistics.setDatasets(List.of(dataset));

        return statistics;
    }

    private List<String> generateColors(int count) {
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // Generate random colors or predefined colors
            colors.add(String.format("#%06x", (int) (Math.random() * 0xFFFFFF)));
        }
        return colors;
    }

    public EmergencyStatistics getEmergencyStatusStatistics() {
        List<Emergency> emergencies = emergencyRepository.findAll();

        // Initialize the map with all possible emergency statuses and set the count to 0
        Map<EmergencyStatus, Integer> statusCount = new EnumMap<>(EmergencyStatus.class);
        for (EmergencyStatus status : EmergencyStatus.values()) {
            statusCount.put(status, 0);
        }

        // Count occurrences of each emergency status
        for (Emergency emergency : emergencies) {
            EmergencyStatus status = emergency.getStatus();
            statusCount.put(status, statusCount.get(status) + 1);
        }

        // Prepare the data for the DTO
        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        List<String> backgroundColor = List.of("#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0"); // Define your colors

        for (Map.Entry<EmergencyStatus, Integer> entry : statusCount.entrySet()) {
            labels.add(entry.getKey().name());
            data.add(entry.getValue());
        }

        Dataset dataset = new Dataset();
        dataset.setLabel("Emergency Status");
        dataset.setData(data);
        dataset.setBackgroundColor(backgroundColor);

        EmergencyStatistics statistics = new EmergencyStatistics();
        statistics.setLabels(labels);
        statistics.setDatasets(List.of(dataset));

        return statistics;
    }

    public Integer getResolvedEmergencyCount() {
        return emergencyRepository.countAllByStatus(EmergencyStatus.RESOLVED);
    }

    public Integer getCancelledEmergencyCount() {
        return emergencyRepository.countAllByStatus(EmergencyStatus.CANCELLED);
    }

    public EmergencyStatistics getEmergencySeverityStatistics() {
        List<Emergency> emergencies = emergencyRepository.findAll();

        // Initialize the map with all possible severities and set the count to 0
        Map<String, Integer> severityCount = new HashMap<>();
        for (Severity severity : Severity.values()) {
            severityCount.put(severity.toString(), 0);
        }
        severityCount.put("No data", 0);

        // Count occurrences of each severity
        for (Emergency emergency : emergencies) {
            Severity severity = emergency.getSeverity();
            if (severity != null) {
                String severityKey = severity.toString();
                severityCount.put(severityKey, severityCount.get(severityKey) + 1);
            } else {
                severityCount.put("No data", severityCount.get("No data") + 1);
            }
        }

        // Prepare the data for the DTO
        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        List<String> backgroundColor = List.of("#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0", "#A1A1A1"); // Define your colors

        for (Map.Entry<String, Integer> entry : severityCount.entrySet()) {
            labels.add(entry.getKey());
            data.add(entry.getValue());
        }

        Dataset dataset = new Dataset();
        dataset.setLabel("Emergency Severity");
        dataset.setData(data);
        dataset.setBackgroundColor(backgroundColor);

        EmergencyStatistics statistics = new EmergencyStatistics();
        statistics.setLabels(labels);
        statistics.setDatasets(List.of(dataset));

        return statistics;
    }

    public EmergencyVolunteerStatistics getVolunteerStatistics() {
        List<Emergency> emergencies = emergencyRepository.findAll();

        int emergenciesWithVolunteers = 0;
        int totalVolunteers = 0;
        Map<String, Integer> volunteerInvolvementCount = new HashMap<>();

        for (Emergency emergency : emergencies) {
            List<User> volunteers = emergency.getVolunteersAccepted();
            if (volunteers != null && !volunteers.isEmpty()) {
                emergenciesWithVolunteers++;
                totalVolunteers += volunteers.size();

                for (User volunteer : volunteers) {
                    String volunteerName = volunteer.getName();
                    volunteerInvolvementCount.put(volunteerName, volunteerInvolvementCount.getOrDefault(volunteerName, 0) + 1);
                }
            }
        }

        double averageVolunteersPerEmergency = emergenciesWithVolunteers > 0 ? (double) totalVolunteers / emergenciesWithVolunteers : 0;

        List<TopVolunteer> topVolunteers = volunteerInvolvementCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    TopVolunteer topVolunteer = new TopVolunteer();
                    topVolunteer.setVolunteerName(entry.getKey());
                    topVolunteer.setInvolvementCount(entry.getValue());
                    return topVolunteer;
                })
                .collect(Collectors.toList());

        EmergencyVolunteerStatistics statistics = new EmergencyVolunteerStatistics();
        statistics.setEmergenciesWithVolunteers(emergenciesWithVolunteers);
        statistics.setAverageVolunteersPerEmergency(averageVolunteersPerEmergency);
        statistics.setTopVolunteers(topVolunteers);

        return statistics;
    }
}
