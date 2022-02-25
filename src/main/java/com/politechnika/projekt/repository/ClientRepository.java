package com.politechnika.projekt.repository;

import com.politechnika.projekt.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {

    Client findByUsername(String username);
}
