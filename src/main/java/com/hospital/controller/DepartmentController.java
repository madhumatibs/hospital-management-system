package com.hospital.controller;
import com.hospital.dto.DepartmentDTO; import com.hospital.entity.Department; import com.hospital.service.DepartmentService; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/departments")
public class DepartmentController extends AbstractCrudController<Department, DepartmentDTO> { public DepartmentController(DepartmentService service){super(service, Department.class, DepartmentDTO.class);} }
