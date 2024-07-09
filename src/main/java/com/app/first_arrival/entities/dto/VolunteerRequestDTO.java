package com.app.first_arrival.entities.dto;

import lombok.Data;

@Data
public class VolunteerRequestDTO {
    private String userName;

    private Long userId;
    private String certificationCode;

    public VolunteerRequestDTO(String userName, String certificationCode, Long userId) {
        this.userName = userName;
        this.certificationCode = certificationCode;
        this.userId = userId;
    }
}
