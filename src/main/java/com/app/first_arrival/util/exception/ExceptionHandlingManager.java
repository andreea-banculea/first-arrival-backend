package com.app.first_arrival.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingManager {
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(Exception exception) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler({ConditionsNotMetException.class})
    public ResponseEntity<ErrorResponse> handleConditionsNotMetException(Exception exception) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler({ForbiddenActionException.class})
    public ResponseEntity<ErrorResponse> handleForbiddenActionException(Exception exception) {
        ErrorResponse response = new ErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(response, response.getStatus());
    }

}
