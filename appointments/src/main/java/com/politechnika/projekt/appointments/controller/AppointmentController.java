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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping()
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getAllAppointmentsByPatientId(@PathVariable Long patientId) {
        return appointmentService.getAllAppointmentsByPatientId(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAllAppointmentsByDoctorId(@PathVariable Long doctorId) {
        return appointmentService.getAllAppointmentsByDoctorId(doctorId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createAppointment(@Valid @RequestBody Appointment appointment) {
        Appointment createdAppointment = null;
        createdAppointment = appointmentService.createAppointment(appointment);
        return ResponseEntity.ok().body(createdAppointment);
    }

    @PatchMapping("/{appointmentId}")
    public ResponseEntity<?> editAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentDto appointmentDto) {
        Appointment editedAppointment = appointmentService.editAppointment(appointmentId, appointmentDto);
        return ResponseEntity.ok().body(editedAppointment);
    }

    @PutMapping("/{appointmentId}/notes")
    public ResponseEntity<?> addAppointmentNotes(@PathVariable Long appointmentId, @RequestBody String notes) {
        Appointment editedAppointment = appointmentService.addAppointmentNotes(appointmentId, notes);
        return ResponseEntity.ok().body(editedAppointment);
    }

    @DeleteMapping(path = "/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }

}
