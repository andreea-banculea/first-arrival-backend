package com.app.first_arrival.util.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@ToString
public class ErrorResponse {
    private HttpStatus status;
    private String message;
}
