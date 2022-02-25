package com.politechnika.projekt.prescriptioncreator.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDto {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private boolean cancelled;
    private LocalDateTime visitTime;
    private Integer visitDuration;
}
