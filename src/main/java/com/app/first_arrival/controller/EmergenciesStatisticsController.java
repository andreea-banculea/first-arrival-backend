package com.app.first_arrival.controller;

import com.app.first_arrival.entities.dto.EmergencyStatistics;
import com.app.first_arrival.entities.dto.EmergencyVolunteerStatistics;
import com.app.first_arrival.service.EmergenciesStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/emergencies/statistics")
public class EmergenciesStatisticsController {
    private final EmergenciesStatisticsService emergenciesStatisticsService;

    private static final Logger logger = Logger.getLogger(EmergenciesStatisticsController.class.getName());

    @Autowired
    public EmergenciesStatisticsController(EmergenciesStatisticsService emergenciesStatisticsService) {
        this.emergenciesStatisticsService = emergenciesStatisticsService;
    }

    @GetMapping("/emergencyTypes")
    public EmergencyStatistics getEmergencyTypesStatistics() {
        return emergenciesStatisticsService.getEmergencyTypeStatistics();
    }

    @GetMapping("/emergencyStatus")
    public EmergencyStatistics getEmergencyStatusStatistics() {
        return emergenciesStatisticsService.getEmergencyStatusStatistics();
    }

    @GetMapping("/count")
    public Integer getEmergencyCount() {
        return emergenciesStatisticsService.getEmergencyCount();
    }

    @GetMapping("/resolvedCount")
    public Integer getResolvedEmergencyCount() {
        return emergenciesStatisticsService.getResolvedEmergencyCount();
    }

    @GetMapping("/cancelledCount")
    public Integer getCancelledEmergencyCount() {
        return emergenciesStatisticsService.getCancelledEmergencyCount();
    }

    @GetMapping("/emergencySeverity")
    public EmergencyStatistics getEmergencySeverityStatistics() {
        return emergenciesStatisticsService.getEmergencySeverityStatistics();
    }

    @GetMapping("/volunteerInvolvement")
    public EmergencyVolunteerStatistics getVolunteerStatistics() {
        return emergenciesStatisticsService.getVolunteerStatistics();
    }
}
