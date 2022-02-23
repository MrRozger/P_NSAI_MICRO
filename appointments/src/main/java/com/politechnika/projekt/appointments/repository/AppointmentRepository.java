package com.politechnika.projekt.appointments.repository;

import com.politechnika.projekt.appointments.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findById(Long id);

    List<Appointment> findAllByPatientId(Long id);

    List<Appointment> findAllByDoctorId(Long id);

}
