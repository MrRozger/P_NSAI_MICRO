package com.politechnika.projekt.service;

import com.politechnika.projekt.exceptions.UserNotFoundException;
import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.model.ClientDTO;
import com.politechnika.projekt.repository.ClientRepository;
import com.politechnika.projekt.repository.RoleRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
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
                    if(StringUtils.isNotBlank(clientDTO.getFirstName())){
                        client.setUsername(clientDTO.getFirstName());
                    }
                    if(StringUtils.isNotBlank(clientDTO.getLastName())){
                        client.setUsername(clientDTO.getLastName());
                    }
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
    public Client findById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new UserNotFoundException("There is no such a user");
        }
        Optional<Client> client = clientRepository.findById(id);
        return client.isPresent()?client.get():null;
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
