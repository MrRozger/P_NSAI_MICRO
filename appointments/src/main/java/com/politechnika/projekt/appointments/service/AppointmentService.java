package com.politechnika.projekt.appointments.service;

import com.politechnika.projekt.appointments.exceptions.AppointmentNotFoundException;
import com.politechnika.projekt.appointments.exceptions.DoctorNotFoundException;
import com.politechnika.projekt.appointments.exceptions.PatientNotFoundException;
import com.politechnika.projekt.appointments.exceptions.UnauthorizedUserException;
import com.politechnika.projekt.appointments.model.Appointment;
import com.politechnika.projekt.appointments.model.AppointmentDto;
import com.politechnika.projekt.appointments.model.ClientDto;
import com.politechnika.projekt.appointments.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Appointment editAppointment(Long appointmentId, AppointmentDto appointmentDto, String username) {
        Appointment existingAppointment = findAppointment(appointmentId, username);

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

    public Appointment cancelAppointment(Long appointmentId, String username) {
        Appointment appointment = findAppointment(appointmentId, username);
        appointment.setCancelled(true);
        return appointmentRepository.save(appointment);
    }

    public Appointment findAppointment(Long appointmentId, String username) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            boolean isPatientLogged = checkCurrentlyLoggedUser(appointment.get().getPatientId(), username);
            boolean isDoctorLogged = checkCurrentlyLoggedUser(appointment.get().getDoctorId(), username);
            if (!isPatientLogged && !isDoctorLogged && !isAdmin(username)) {
                throw new UnauthorizedUserException("Currently logged user does not have an access");
            }
            return appointment.get();
        }
        throw new AppointmentNotFoundException("Appointment does not exist");
    }

    public List<Appointment> getAllAppointmentsByPatientId(Long patientId, String username) {
        String role = clientServiceFeignClient.getRoleByClientId(patientId);
        if (("ROLE_PATIENT".equals(role) && checkCurrentlyLoggedUser(patientId, username)) || isAdmin(username)) {
            return appointmentRepository.findAllByPatientId(patientId);
        }
        throw new PatientNotFoundException("Patient was not found");
    }

    public List<Appointment> getAllAppointmentsByDoctorId(Long doctorId, String username) {
        String role = clientServiceFeignClient.getRoleByClientId(doctorId);
        if (("ROLE_DOCTOR".equals(role) && checkCurrentlyLoggedUser(doctorId, username)) || isAdmin(username)) {
            return appointmentRepository.findAllByDoctorId(doctorId);
        }
        throw new DoctorNotFoundException("Doctor was not found");
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    private boolean checkCurrentlyLoggedUser(Long clientId, String username) {
        ClientDto client = clientServiceFeignClient.getClient(clientId);
        return username.equals(client.getUsername());
    }

    private boolean isAdmin(String username) {
        return "ROLE_ADMIN".equals(clientServiceFeignClient.getRoleByClientUsername(username));
    }

}
