package com.politechnika.projekt.appointments.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "PatientId cannot be null")
    private Long patientId;

    @NotNull(message = "DoctorId cannot be null")
    private Long doctorId;

    private boolean cancelled;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @FutureOrPresent(message = "Date cannot be from the past")
    private LocalDateTime visitTime;

    private Integer visitDuration = 15;
}
