package com.politechnika.projekt.appointments.model;

import lombok.Data;

@Data
public class ClientDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
