package com.politechnika.projekt.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ClientDTO {

    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private String email;

}
