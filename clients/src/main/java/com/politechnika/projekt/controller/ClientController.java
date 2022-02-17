package com.politechnika.projekt.controller;

import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.model.ClientDTO;
import com.politechnika.projekt.repository.ClientRepository;
import com.politechnika.projekt.service.ClientService;
import com.politechnika.projekt.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {


    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final EmailService emailService;

    public ClientController(ClientService clientService, ClientRepository clientRepository, EmailService emailService) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.emailService = emailService;
    }

    @GetMapping("/all")
    public List<Client> getAllClients() {
        return clientService.findAll();
    }

    @GetMapping("/{clientId}")
    public void getParticipantById(@PathVariable Long clientId) {
        clientService.findById(clientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveClient(@RequestBody Client client) {
        clientService.createClient(client);
        emailService.sendEmail(client.getUsername());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "/{id}")
    public void editPatient(@PathVariable Long id, @RequestBody ClientDTO client) {
        clientService.editClient(id, client);
    }

    @DeleteMapping(path = "/{id}")
    public void deletePatient(@PathVariable Long id) {
        clientService.removeClient(id);
    }

}
