package com.politechnika.projekt.controller;

import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.model.ClientDTO;
import com.politechnika.projekt.repository.ClientRepository;
import com.politechnika.projekt.service.ClientService;
import com.politechnika.projekt.service.EmailService;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
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
    public Client getParticipantById(@PathVariable Long clientId) {
        return clientService.findById(clientId);
    }

    @GetMapping("/id/{clientId}/role")
    public String getRoleByClientId(@PathVariable Long clientId) {
        return clientService.getRoleByClientId(clientId);
    }

    @GetMapping("/username/{username}/role")
    public String getRoleByClientUsername(@PathVariable String username) {
        return clientService.getRoleByClientUsername(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveClient(@RequestBody Client client) {
        Client createdClient = clientService.createClient(client);
        emailService.sendEmail(client.getUsername());
        return ResponseEntity.ok().body(createdClient);
    }

    @PutMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public void putPatient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        clientService.putClient(id, clientDTO);
    }

    @RolesAllowed("ROLE_PATIENT")
    @PatchMapping("/{clientId}")
    public ResponseEntity<?> editPatient(@PathVariable Long clientId, @RequestBody ClientDTO clientDTO, Principal user) {
        Client editedClient = clientService.editClient(clientId,clientDTO,user.getName());
        return ResponseEntity.ok().body(editedClient);
    }

    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        clientService.removeClient(id);
    }

}
