package com.politechnika.projekt.prescriptioncreator.service;

import com.politechnika.projekt.prescriptioncreator.model.AppointmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "APPOINTMENTS-SERVICE")
public interface AppointmentServiceFeignClient {

    @GetMapping("/appointments/{appointmentId}")
    AppointmentDto findAppointment(@PathVariable Long appointmentId);

    @GetMapping("/appointments/")
    List<AppointmentDto> getAllAppointments();

}
