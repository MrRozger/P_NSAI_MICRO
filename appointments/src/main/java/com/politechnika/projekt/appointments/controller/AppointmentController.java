package com.politechnika.projekt.appointments.controller;

import com.politechnika.projekt.appointments.model.Appointment;
import com.politechnika.projekt.appointments.model.AppointmentDto;
import com.politechnika.projekt.appointments.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping()
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{appointmentId}")
    public Appointment findAppointment(@PathVariable Long appointmentId, Principal user) {
        return appointmentService.findAppointment(appointmentId, user.getName());
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_PATIENT"})
    @GetMapping("/patient/{patientId}")
    public List<Appointment> getAllAppointmentsByPatientId(@PathVariable Long patientId, Principal user) {
        return appointmentService.getAllAppointmentsByPatientId(patientId, user.getName());
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_DOCTOR"})
    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAllAppointmentsByDoctorId(@PathVariable Long doctorId, Principal user) {
        return appointmentService.getAllAppointmentsByDoctorId(doctorId, user.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createAppointment(@Valid @RequestBody Appointment appointment) {
        Appointment createdAppointment = appointmentService.createAppointment(appointment);
        return ResponseEntity.ok().body(createdAppointment);
    }

    @RolesAllowed("ROLE_PATIENT")
    @PatchMapping("/{appointmentId}")
    public ResponseEntity<?> editAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentDto appointmentDto, Principal user) {
        Appointment editedAppointment = appointmentService.editAppointment(appointmentId, appointmentDto, user.getName());
        return ResponseEntity.ok().body(editedAppointment);
    }

    @DeleteMapping(path = "/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId, Principal user) {
        appointmentService.cancelAppointment(appointmentId, user.getName());
        return ResponseEntity.ok().build();
    }

}
