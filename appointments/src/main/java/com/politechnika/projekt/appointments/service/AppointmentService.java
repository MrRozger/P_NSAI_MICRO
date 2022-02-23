package com.politechnika.projekt.appointments.service;

import com.politechnika.projekt.appointments.exceptions.AppointmentNotFoundException;
import com.politechnika.projekt.appointments.exceptions.PatientOrDoctorNotFoundException;
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
        if (doctor != null && patient != null) {
            return appointmentRepository.save(appointment);
        }
        throw new PatientOrDoctorNotFoundException("Patient or doctor was not found. Cannot create an appointment");
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
        return appointmentRepository.findAllByPatientId(patientId);
    }

    public List<Appointment> getAllAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findAllByDoctorId(doctorId);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
