package com.hospital.controller;
import com.hospital.dto.PatientDTO;
import com.hospital.entity.Patient;
import com.hospital.service.PatientService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.hospital.repository.PatientRepository;
import java.time.LocalDate;
import java.util.List;
@RestController @RequestMapping("/api/v1/patients")
public class PatientController extends AbstractCrudController<Patient, PatientDTO> {
    private final PatientRepository patientRepository;
    public PatientController(PatientService service, PatientRepository repository){super(service, Patient.class, PatientDTO.class);this.patientRepository=repository;}
    @GetMapping("/reports/admissions") public List<PatientDTO> admissions(@RequestParam LocalDate start,@RequestParam LocalDate end){if(start.isAfter(end))throw new IllegalArgumentException("Start date must be on or before end date.");return patientRepository.findByAdmissionDateBetween(start,end).stream().map(this::toDto).toList();}
    @GetMapping("/reports/ailment") public List<PatientDTO> ailment(@RequestParam String ailment){return patientRepository.findByAilmentContainingIgnoreCase(ailment).stream().map(this::toDto).toList();}
    @GetMapping("/reports/doctor") public List<PatientDTO> doctorReport(@RequestParam String doctor){return patientRepository.findByAssignedDoctorIgnoreCase(doctor).stream().map(this::toDto).toList();}
    @GetMapping("/reports/doctor-count") public java.util.Map<String,Long> doctorCount(){return patientRepository.findAll().stream().collect(java.util.stream.Collectors.groupingBy(p->java.util.Optional.ofNullable(p.getAssignedDoctor()).orElse("Unassigned"),java.util.stream.Collectors.counting()));}
}
