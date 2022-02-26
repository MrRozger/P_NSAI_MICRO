package com.politechnika.projekt.service;

import com.politechnika.projekt.exceptions.UnauthorizedUserException;
import com.politechnika.projekt.exceptions.UserNotFoundException;
import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.model.ClientDTO;
import com.politechnika.projekt.repository.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {


    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;


    public ClientServiceImpl(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Client createClient(Client client) {
        client.setPassword(hashPassword(client.getPassword()));
        return clientRepository.save(client);
    }

    @Override
    public void putClient(Long id, ClientDTO clientDTO) {
        Optional<Client> existingClient = clientRepository.findById(id);

        existingClient.ifPresentOrElse(
                client -> {
                    modelMapper.map(clientDTO, client);
                    clientRepository.save(client);
                },
                () -> {
                    throw new UserNotFoundException("There is no such a user");
                }
        );
    }


    @Override
    public Client editClient(Long id, ClientDTO clientDTO, String username)  {
        Client existingClient = findClient(id,username);

                    if (clientDTO.getFirstName() != null) {
                        existingClient.setFirstName(clientDTO.getFirstName());
                    }
                    if (clientDTO.getLastName() != null) {
                        existingClient.setLastName(clientDTO.getLastName());
                    }
                    if (clientDTO.getUsername() != null) {
                        existingClient.setUsername(clientDTO.getUsername());
                    }
                    if (clientDTO.getEmail() != null) {
                        existingClient.setEmail(clientDTO.getEmail());
                    }
                    if (clientDTO.getPassword() != null) {
                        existingClient.setPassword(clientDTO.getPassword());
                    }
                   return clientRepository.save(existingClient);

                }



    @Override
    public  Client findClient(Long clientId, String username) {
        Optional<Client> client = clientRepository.findById(clientId);
        if(client.isPresent()){
            boolean isPatientLogged = checkCurrentlyLoggedUser(client.get().getId(), username);
            if(!isPatientLogged)  {
                throw new UnauthorizedUserException("Currently logged user does not have an access");
            }
        return client.get();
        }
        throw new UserNotFoundException("User not found");
    }

    @Override
    public boolean checkCurrentlyLoggedUser(Long clientId, String username) {
        Client client = findById(clientId);
        return username.equals(client.getUsername());
    }

    @Override
    public boolean removeClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new UserNotFoundException("There is no such a user");
        }
        clientRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new UserNotFoundException("There is no such a user");
        }
        Optional<Client> client = clientRepository.findById(id);
        return client.isPresent() ? client.get() : null;
    }

    @Override
    public String getRoleByClientId(Long clientId) {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            throw new UserNotFoundException("There is no such a user");
        }
        return client.get().getRole().name();
    }

    @Override
    public String getRoleByClientUsername(String username) {
        Optional<Client> client = clientRepository.findByUsername(username);
        if (client.isEmpty()) {
            throw new UserNotFoundException("There is no such a user");
        }
        return client.get().getRole().name();
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
