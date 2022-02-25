package com.politechnika.projekt.appointments.service;

import com.politechnika.projekt.appointments.exceptions.AppointmentNotFoundException;
import com.politechnika.projekt.appointments.exceptions.DoctorNotFoundException;
import com.politechnika.projekt.appointments.exceptions.PatientNotFoundException;
import com.politechnika.projekt.appointments.model.Appointment;
import com.politechnika.projekt.appointments.model.AppointmentDto;
import com.politechnika.projekt.appointments.model.ClientDto;
import com.politechnika.projekt.appointments.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClientServiceFeignClient clientServiceFeignClient;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, ClientServiceFeignClient clientServiceFeignClient) {
        this.appointmentRepository = appointmentRepository;
        this.clientServiceFeignClient = clientServiceFeignClient;
    }

    public Appointment createAppointment(Appointment appointment) {
        ClientDto doctor = clientServiceFeignClient.getClient(appointment.getDoctorId());
        ClientDto patient = clientServiceFeignClient.getClient(appointment.getPatientId());
        if (doctor == null || !"ROLE_DOCTOR".equals(doctor.getRole())) {
            throw new DoctorNotFoundException("Doctor was not found");
        }
        if (patient == null || !"ROLE_PATIENT".equals(patient.getRole())) {
            throw new PatientNotFoundException("Patient was not found");
        }
        return appointmentRepository.save(appointment);
    }

    public Appointment editAppointment(Long appointmentId, AppointmentDto appointmentDto) {
        Appointment existingAppointment = findAppointment(appointmentId);

        if (appointmentDto.getDoctorId() != null) {
            existingAppointment.setDoctorId(appointmentDto.getDoctorId());
        }
        if (appointmentDto.getPatientId() != null) {
            existingAppointment.setPatientId(appointmentDto.getPatientId());
        }
        if (appointmentDto.getVisitDuration() != null) {
            existingAppointment.setVisitDuration(appointmentDto.getVisitDuration());
        }
        if (appointmentDto.getVisitTime() != null) {
            existingAppointment.setVisitTime(appointmentDto.getVisitTime());
        }
        return appointmentRepository.save(existingAppointment);
    }

    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = findAppointment(appointmentId);
        appointment.setCancelled(true);
        return appointmentRepository.save(appointment);
    }

    public Appointment findAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException("Appointment does not exist"));
    }

    public List<Appointment> getAllAppointmentsByPatientId(Long patientId) {
        String role = clientServiceFeignClient.getRoleByClientId(patientId);
        if ("ROLE_PATIENT".equals(role)) {
            return appointmentRepository.findAllByPatientId(patientId);
        }
        throw new PatientNotFoundException("Patient was not found");
    }

    public List<Appointment> getAllAppointmentsByDoctorId(Long doctorId) {
        String role = clientServiceFeignClient.getRoleByClientId(doctorId);
        if ("ROLE_DOCTOR".equals(role)) {
            return appointmentRepository.findAllByDoctorId(doctorId);
        }
        throw new DoctorNotFoundException("Doctor was not found");
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment addAppointmentNotes(Long appointmentId, String notes) {
        Appointment existingAppointment = findAppointment(appointmentId);
        existingAppointment.setNotes(notes);
        return appointmentRepository.save(existingAppointment);
    }
}
