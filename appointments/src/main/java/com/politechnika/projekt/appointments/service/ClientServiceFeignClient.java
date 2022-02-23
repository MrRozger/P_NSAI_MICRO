package com.politechnika.projekt.appointments.service;

import com.politechnika.projekt.appointments.model.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="CLIENT-SERVICE")
public interface ClientServiceFeignClient {

    @GetMapping("/clients/all")
    List<ClientDto> getAllClients();

    @GetMapping("/clients/{clientId}")
    ClientDto getClient(@PathVariable Long clientId);


}
