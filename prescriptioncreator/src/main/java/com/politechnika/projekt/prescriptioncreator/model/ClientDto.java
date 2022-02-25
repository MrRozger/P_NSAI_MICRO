package com.politechnika.projekt.prescriptioncreator.model;

import lombok.Data;

@Data
public class ClientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String role;
}
