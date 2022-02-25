package com.politechnika.projekt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;



public enum Role {

    ROLE_ADMIN,
    ROLE_DOCTOR,
    ROLE_PATIENT;

}
/*@Entity
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String role;
}*/
