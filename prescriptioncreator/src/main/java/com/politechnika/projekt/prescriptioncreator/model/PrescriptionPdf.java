package com.politechnika.projekt.prescriptioncreator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PrescriptionPdf {

    private Long appointmentId;
    private String patientFirstName;
    private String patientLastName;
    private String doctorFirstName;
    private String doctorLastName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime visitTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime prescriptionDueDate;

    private String notes;
    private List<String> drugs;
}
