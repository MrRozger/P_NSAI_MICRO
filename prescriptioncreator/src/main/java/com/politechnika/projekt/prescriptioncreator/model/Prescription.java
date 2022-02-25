package com.politechnika.projekt.prescriptioncreator.model;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Prescription {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "AppointmentId cannot be null")
    private Long appointmentId;

    private String notes;

    @ElementCollection
    private List<String> drugs = new ArrayList<>();

}
