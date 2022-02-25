package com.politechnika.projekt.prescriptioncreator.repository;

import com.politechnika.projekt.prescriptioncreator.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    Optional<Prescription> findById(Long id);

    Optional<Prescription> findByAppointmentId(Long id);

}
