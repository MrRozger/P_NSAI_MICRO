package com.politechnika.projekt.service;

import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.model.ClientDTO;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;


public interface ClientService {

    Client createClient(Client client);

    void putClient(Long id, ClientDTO clientDTO);

    Client editClient(Long id, ClientDTO clientDTO, String username);

    boolean removeClient(Long id);

    List<Client> findAll();

    Client findById(Long id);

    String getRoleByClientId(Long clientId);

    String getRoleByClientUsername(String username);

    Client findClient(Long clientId, String username) throws UserPrincipalNotFoundException;

    boolean checkCurrentlyLoggedUser(Long clientId, String username);
}
