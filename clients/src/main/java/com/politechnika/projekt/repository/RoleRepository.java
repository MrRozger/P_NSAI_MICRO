package com.politechnika.projekt.repository;

import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(String role);

}
