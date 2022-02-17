package com.politechnika.projekt.utils;

import com.politechnika.projekt.model.Role;
import com.politechnika.projekt.repository.RoleRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InsertDataToDB {


    private RoleRepository roleRepository;

    public InsertDataToDB(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void InsertData() {
        Role admin = new Role();
        Role doctor = new Role();
        Role patient = new Role();

        admin.setRole("ROLE_ADMIN");
        doctor.setRole("ROLE_DOCTOR");
        patient.setRole("ROLE_PATIENT");

        roleRepository.save(admin);
        roleRepository.save(doctor);
        roleRepository.save(patient);


    }
}
