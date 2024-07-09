package com.app.first_arrival.entities.dto;

import lombok.Data;

@Data
public class LocationUpdateDTO {
    private Long userId;
    private double latitude;
    private double longitude;
}