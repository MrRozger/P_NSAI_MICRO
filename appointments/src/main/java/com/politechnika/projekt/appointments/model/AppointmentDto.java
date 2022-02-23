package com.politechnika.projekt.appointments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDto {

    private Long patientId;

    private Long doctorId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime visitTime;

    private Integer visitDuration = 15;
}
