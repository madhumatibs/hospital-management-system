package com.hospital.controller;
import com.hospital.dto.MedicalRecordDTO; import com.hospital.entity.MedicalRecord; import com.hospital.service.MedicalRecordService; import org.springframework.web.bind.annotation.*; import com.hospital.repository.MedicalRecordRepository; import java.util.List;
@RestController @RequestMapping("/api/v1/medical-records")
public class MedicalRecordController extends AbstractCrudController<MedicalRecord, MedicalRecordDTO> {
    private final MedicalRecordRepository recordRepository;
    public MedicalRecordController(MedicalRecordService service, MedicalRecordRepository repository){super(service, MedicalRecord.class, MedicalRecordDTO.class);this.recordRepository=repository;}
    @GetMapping("/patient/{patientId}") public List<MedicalRecordDTO> byPatient(@PathVariable Integer patientId){return recordRepository.findByPatientId(patientId).stream().map(this::toDto).toList();}
    @GetMapping("/doctor/{doctorId}") public List<MedicalRecordDTO> byDoctor(@PathVariable Integer doctorId){return recordRepository.findByDoctorId(doctorId).stream().map(this::toDto).toList();}
}
