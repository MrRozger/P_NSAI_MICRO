package com.politechnika.projekt.service;

import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.model.Email;
import com.politechnika.projekt.repository.ClientRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class EmailServiceImpl implements EmailService {

    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;
    private final ClientRepository clientRepository;

    public EmailServiceImpl(RabbitTemplate rabbitTemplate, RestTemplate restTemplate, ClientRepository clientRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
        this.clientRepository = clientRepository;
    }

    @Override
    public void sendEmail(String username) {

/*        ClientDTO clientDTO = restTemplate.getForObject("http://localhost:8090/clients/"+clientId,ClientDTO.class);
                //HttpMethod.GET, HttpEntity.EMPTY, ClientDTO.class).getBody(); Czemu nie przekazuje do clientDTO uzytkownika po ID*/

        Client client = clientRepository.findByUsername(username);
        Email email = createEmail(client);

        rabbitTemplate.convertAndSend("239081", email);
    }

    @Override
    public Email createEmail(Client client) {
        Email email = new Email();
        email.setTo(client.getEmail());
        email.setSubject("Witaj " + client.getUsername());
        email.setBody("Cieszymy sie, że skorzystales z naszych usług " + client.getUsername());
        return email;
    }

}
