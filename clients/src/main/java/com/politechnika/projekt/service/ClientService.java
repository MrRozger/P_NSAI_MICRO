package com.politechnika.projekt.service;

import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.model.ClientDTO;

import java.util.List;
import java.util.Optional;


public interface ClientService {

    void createClient(Client client);

    void editClient(Long id, ClientDTO clientDTO);

    boolean removeClient(Long id);

    List<Client> findAll();

    Optional<Client> findById(Long id);

}
