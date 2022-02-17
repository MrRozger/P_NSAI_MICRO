package com.politechnika.projekt.model;

import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;

@Getter
@Setter
public class Email implements Serializable {

    private String to;
    private String subject;
    private String body;
}