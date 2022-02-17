package com.politechnika.projekt.service;

import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.model.ClientDTO;
import com.politechnika.projekt.model.Email;

public interface EmailService {

    Email createEmail(Client client);

    void sendEmail(String username);
}
