package com.politechnika.projekt.prescriptioncreator.service;

import com.politechnika.projekt.prescriptioncreator.configuration.OAuthFeignConfiguration;
import com.politechnika.projekt.prescriptioncreator.model.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CLIENT-SERVICE", configuration = OAuthFeignConfiguration.class)
public interface ClientServiceFeignClient {

    @GetMapping("/clients/all")
    List<ClientDto> getAllClients();

    @GetMapping("/clients/{clientId}")
    ClientDto getClient(@PathVariable Long clientId);

    @GetMapping("/clients/id/{clientId}/role")
    String getRoleByClientId(@PathVariable Long clientId);

    @GetMapping("/clients/username/{username}/role")
    String getRoleByClientUsername(@PathVariable String username);

}
