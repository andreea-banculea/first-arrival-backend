package com.app.first_arrival.util.helpers;

import lombok.Data;

@Data
public class TokenRequest {
    private Long userId;
    private String token;

}