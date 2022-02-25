package com.politechnika.projekt.prescriptioncreator.service;

import com.politechnika.projekt.prescriptioncreator.exceptions.DoctorNotFoundException;
import com.politechnika.projekt.prescriptioncreator.exceptions.PatientNotFoundException;
import com.politechnika.projekt.prescriptioncreator.model.AppointmentDto;
import com.politechnika.projekt.prescriptioncreator.model.ClientDto;
import com.politechnika.projekt.prescriptioncreator.model.Prescription;
import com.politechnika.projekt.prescriptioncreator.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ClientServiceFeignClient clientServiceFeignClient;
    private final AppointmentServiceFeignClient appointmentServiceFeignClient;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository, ClientServiceFeignClient clientServiceFeignClient, AppointmentServiceFeignClient appointmentServiceFeignClient) {
        this.prescriptionRepository = prescriptionRepository;
        this.clientServiceFeignClient = clientServiceFeignClient;
        this.appointmentServiceFeignClient = appointmentServiceFeignClient;
    }

    public Prescription createPrescription(Prescription prescription) {
        AppointmentDto appointment = appointmentServiceFeignClient.findAppointment(prescription.getAppointmentId());
        ClientDto doctor = clientServiceFeignClient.getClient(appointment.getDoctorId());
        ClientDto patient = clientServiceFeignClient.getClient(appointment.getPatientId());
        if (doctor == null || !"ROLE_DOCTOR".equals(doctor.getRole())) {
            throw new DoctorNotFoundException("Doctor was not found");
        }
        if (patient == null || !"ROLE_PATIENT".equals(patient.getRole())) {
            throw new PatientNotFoundException("Patient was not found");
        }
        return prescriptionRepository.save(prescription);
    }

    public void deletePrescription(Long prescriptionId) {
        prescriptionRepository.deleteById(prescriptionId);
    }

    public List<Prescription> getAllPrescriptionsByPatientId(Long patientId) {
        List<AppointmentDto> allAppointments = appointmentServiceFeignClient.getAllAppointments();

        return allAppointments.stream()
                .filter(a -> a.getPatientId() == patientId)
                .filter(a -> "ROLE_PATIENT".equals(getRole(a.getPatientId())))
                .map(a -> findPrescription(a.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Prescription> getAllPrescriptionsByDoctorId(Long doctorId) {
        List<AppointmentDto> allAppointments = appointmentServiceFeignClient.getAllAppointments();

        return allAppointments.stream()
                .filter(a -> a.getDoctorId() == doctorId)
                .filter(a -> "ROLE_DOCTOR".equals(getRole(a.getDoctorId())))
                .map(a -> findPrescription(a.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Prescription findPrescription(Long appointmentId) {
        Optional<Prescription> prescription = prescriptionRepository.findByAppointmentId(appointmentId);
        return prescription.isPresent() ? prescription.get() : null;
    }

    private String getRole(Long clientId) {
        return clientServiceFeignClient.getRoleByClientId(clientId);
    }
}
