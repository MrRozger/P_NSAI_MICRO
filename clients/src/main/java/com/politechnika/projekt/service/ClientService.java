package com.politechnika.projekt.service;

import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.model.ClientDTO;

import java.util.List;
import java.util.Optional;


public interface ClientService {

    Client createClient(Client client);

    void putClient(Long id, ClientDTO clientDTO);

    void editClient(Long id, ClientDTO clientDTO);

    boolean removeClient(Long id);

    List<Client> findAll();

    Client findById(Long id);

}
