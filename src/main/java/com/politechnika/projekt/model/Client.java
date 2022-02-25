package com.politechnika.projekt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Username must not be null!")
    private String username;


    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull(message = "Account's password must not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "Email cannot be null")
    @Email
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_PATIENT;

/*    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> role = new HashSet<>(0);*/

}