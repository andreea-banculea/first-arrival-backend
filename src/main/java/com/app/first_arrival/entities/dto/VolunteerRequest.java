package com.app.first_arrival.entities.dto;

import lombok.Data;

@Data
public class VolunteerRequest {
    private Long userId;
    private String certificationCode;

    public VolunteerRequest(Long id, String certificationCode) {
        this.userId = id;
        this.certificationCode = certificationCode;
    }
}
