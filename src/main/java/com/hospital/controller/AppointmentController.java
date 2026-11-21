package com.hospital.controller;
import com.hospital.dto.AppointmentDTO; import com.hospital.entity.Appointment; import com.hospital.service.AppointmentService; import org.springframework.web.bind.annotation.*; import com.hospital.repository.AppointmentRepository; import java.time.LocalDate; import java.util.List;
@RestController @RequestMapping("/api/v1/appointments")
public class AppointmentController extends AbstractCrudController<Appointment, AppointmentDTO> {
    private final AppointmentRepository appointmentRepository;
    public AppointmentController(AppointmentService service, AppointmentRepository repository){super(service, Appointment.class, AppointmentDTO.class);this.appointmentRepository=repository;}
    @GetMapping("/doctor/{doctorId}") public List<AppointmentDTO> byDoctor(@PathVariable Integer doctorId){return appointmentRepository.findByDoctorId(doctorId).stream().map(this::toDto).toList();}
    @GetMapping("/patient/{patientId}") public List<AppointmentDTO> byPatient(@PathVariable Integer patientId){return appointmentRepository.findByPatientId(patientId).stream().map(this::toDto).toList();}
    @GetMapping("/date") public List<AppointmentDTO> byDate(@RequestParam LocalDate date){return appointmentRepository.findByAppointmentDate(date).stream().map(this::toDto).toList();}
}
