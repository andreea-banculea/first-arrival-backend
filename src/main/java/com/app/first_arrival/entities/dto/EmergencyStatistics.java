package com.app.first_arrival.entities.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmergencyStatistics {
    private List<String> labels;
    private List<Dataset> datasets;
}
