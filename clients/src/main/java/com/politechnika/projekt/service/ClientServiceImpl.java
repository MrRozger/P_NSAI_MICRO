package com.politechnika.projekt.service;

import com.netflix.discovery.converters.Auto;
import com.politechnika.projekt.exceptions.UserNotFoundException;
import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.model.ClientDTO;
import com.politechnika.projekt.repository.ClientRepository;
import com.politechnika.projekt.repository.RoleRepository;
import javassist.NotFoundException;
import org.apache.commons.lang.StringUtils;
import org.apache.coyote.Response;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {


    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;


    public ClientServiceImpl(ClientRepository clientRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Client createClient(Client client) {
        client.getClientRole().add(roleRepository.findByRole("ROLE_PATIENT").orElse(null));
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
    public void editClient(Long id, ClientDTO clientDTO) {
        Optional<Client> existingClient = clientRepository.findById(id);

        existingClient.ifPresentOrElse(
                client -> {
                    if(StringUtils.isNotBlank(clientDTO.getUsername())){
                        client.setUsername(clientDTO.getUsername());
                    }
                    if(StringUtils.isNotBlank(clientDTO.getEmail())){
                        client.setUsername(clientDTO.getEmail());
                    }
                    if(StringUtils.isNotBlank(clientDTO.getPassword())){
                        client.setUsername(clientDTO.getPassword());
                    }
                    clientRepository.save(client);
                },
                () -> {
                    throw new UserNotFoundException("There is no such a user");
                }
        );
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
    public Optional<Client> findById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new UserNotFoundException("There is no such a user");
        }
        return clientRepository.findById(id);
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
