package com.hospital.controller;
import com.hospital.dto.DoctorDTO; import com.hospital.entity.Doctor; import com.hospital.service.DoctorService; import org.springframework.web.bind.annotation.*; import com.hospital.repository.DoctorRepository; import java.util.List;
@RestController @RequestMapping("/api/v1/doctors")
public class DoctorController extends AbstractCrudController<Doctor, DoctorDTO> {
    private final DoctorRepository doctorRepository;
    public DoctorController(DoctorService service, DoctorRepository repository){super(service, Doctor.class, DoctorDTO.class);this.doctorRepository=repository;}
    @GetMapping("/specialization") public List<DoctorDTO> specialization(@RequestParam String name){return doctorRepository.findBySpecializationIgnoreCase(name).stream().map(this::toDto).toList();}
    @GetMapping("/department/{departmentId}") public List<DoctorDTO> byDepartment(@PathVariable Integer departmentId){return doctorRepository.findByDepartmentId(departmentId).stream().map(this::toDto).toList();}
}
