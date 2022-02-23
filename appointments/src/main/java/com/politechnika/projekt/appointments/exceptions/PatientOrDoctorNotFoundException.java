package com.politechnika.projekt.appointments.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientOrDoctorNotFoundException extends RuntimeException {
    public PatientOrDoctorNotFoundException(String message) {
        super(message);
    }
}
