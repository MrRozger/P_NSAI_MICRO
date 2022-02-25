package com.politechnika.projekt.prescriptioncreator.service;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.politechnika.projekt.prescriptioncreator.exceptions.PrescriptionNotFoundException;
import com.politechnika.projekt.prescriptioncreator.model.AppointmentDto;
import com.politechnika.projekt.prescriptioncreator.model.ClientDto;
import com.politechnika.projekt.prescriptioncreator.model.Prescription;
import com.politechnika.projekt.prescriptioncreator.model.PrescriptionPdf;
import com.politechnika.projekt.prescriptioncreator.repository.PrescriptionRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private final PrescriptionRepository prescriptionRepository;
    private final ClientServiceFeignClient clientServiceFeignClient;
    private final AppointmentServiceFeignClient appointmentServiceFeignClient;

    @Autowired
    public PdfService(PrescriptionRepository prescriptionRepository, ClientServiceFeignClient clientServiceFeignClient, AppointmentServiceFeignClient appointmentServiceFeignClient) {
        this.prescriptionRepository = prescriptionRepository;
        this.clientServiceFeignClient = clientServiceFeignClient;
        this.appointmentServiceFeignClient = appointmentServiceFeignClient;
    }


    public void generatePdf(Long prescriptionId, HttpServletResponse response) {
        PrescriptionPdf prescriptionData = getPrescriptionData(prescriptionId);
        try {
            OutputStream o = response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + prescriptionData.getPatientLastName() + prescriptionData.getAppointmentId() + ".pdf");
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, o);
            pdf.open();
            pdf.add(new Paragraph("NSAI - Prescription Creator"));
            pdf.add(new Paragraph(Chunk.NEWLINE));
            pdf.add(getPdfPTable(prescriptionData));
            pdf.close();
            o.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private PdfPTable getPdfPTable(PrescriptionPdf prescriptionData) {
        PdfPTable table = new PdfPTable(2);
        table.addCell("Doctor");
        table.addCell(prescriptionData.getDoctorFirstName() + " " + prescriptionData.getDoctorLastName());
        table.addCell("Patient");
        table.addCell(prescriptionData.getPatientFirstName()+ " " + prescriptionData.getPatientLastName());
        table.addCell("Visit Date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        table.addCell(prescriptionData.getVisitTime().format(formatter));
        table.addCell("Prescription Due Date");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        table.addCell(prescriptionData.getPrescriptionDueDate().format(formatter2));
        table.addCell("Doctor notes");
        table.addCell(prescriptionData.getNotes());
        table.addCell("Prescribed drugs");
        table.addCell(StringUtils.join(prescriptionData.getDrugs(), ", "));
        return table;
    }

    public PrescriptionPdf getPrescriptionData(Long prescriptionId) {
        Prescription prescription = findPrescription(prescriptionId);
        AppointmentDto appointment = appointmentServiceFeignClient.findAppointment(prescription.getAppointmentId());
        ClientDto doctor = clientServiceFeignClient.getClient(appointment.getDoctorId());
        ClientDto patient = clientServiceFeignClient.getClient(appointment.getPatientId());

        PrescriptionPdf prescriptionPdf = new PrescriptionPdf();
        prescriptionPdf.setAppointmentId(appointment.getId());
        prescriptionPdf.setPatientFirstName(patient.getFirstName());
        prescriptionPdf.setPatientLastName(patient.getLastName());
        prescriptionPdf.setDoctorFirstName(doctor.getFirstName());
        prescriptionPdf.setDoctorLastName(doctor.getLastName());
        prescriptionPdf.setVisitTime(appointment.getVisitTime());
        prescriptionPdf.setPrescriptionDueDate(appointment.getVisitTime().plusDays(30));
        prescriptionPdf.setNotes(prescription.getNotes());
        prescriptionPdf.setDrugs(prescription.getDrugs());

        return prescriptionPdf;
    }

    private Prescription findPrescription(Long prescriptionId) {
        return prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new PrescriptionNotFoundException("Appointment does not exist"));
    }
}
