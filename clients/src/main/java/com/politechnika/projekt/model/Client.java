package com.politechnika.projekt.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Username must not be null!")
    private String username;

    @NotNull(message = "Account's password must not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "Email cannot be null")
    @Email
    private String email;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> clientRole = new HashSet<>(0);
}
