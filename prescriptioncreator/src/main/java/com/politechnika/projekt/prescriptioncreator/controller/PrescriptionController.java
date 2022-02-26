package com.politechnika.projekt.prescriptioncreator.controller;

import com.politechnika.projekt.prescriptioncreator.model.Prescription;
import com.politechnika.projekt.prescriptioncreator.service.PdfService;
import com.politechnika.projekt.prescriptioncreator.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final PdfService pdfService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService, PdfService pdfService) {
        this.prescriptionService = prescriptionService;
        this.pdfService = pdfService;
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_PATIENT"})
    @GetMapping("/patient/{patientId}")
    public List<Prescription> getAllPrescriptionsByPatientId(@PathVariable Long patientId) {
        return prescriptionService.getAllPrescriptionsByPatientId(patientId);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_DOCTOR"})
    @GetMapping("/doctor/{doctorId}")
    public List<Prescription> getAllPrescriptionsByDoctorId(@PathVariable Long doctorId) {
        return prescriptionService.getAllPrescriptionsByDoctorId(doctorId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPrescription(@Valid @RequestBody Prescription prescription) {
        Prescription createdAppointment = prescriptionService.createPrescription(prescription);
        return ResponseEntity.ok().body(createdAppointment);
    }

    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/{prescriptionId}")
    public ResponseEntity<?> deletePrescription(@PathVariable Long prescriptionId) {
        prescriptionService.deletePrescription(prescriptionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pdf/{appointmentId}")
    public void generatePdf(@PathVariable Long appointmentId, HttpServletResponse response) {
        pdfService.generatePdf(appointmentId, response);
    }

}
