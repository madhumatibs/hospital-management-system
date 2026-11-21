package com.hospital.controller;
import com.hospital.dto.AppointmentDTO; import com.hospital.dto.PatientDTO; import com.hospital.entity.Appointment; import com.hospital.entity.Patient; import com.hospital.repository.*; import org.springframework.beans.BeanUtils; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1/reports")
public class ReportController {
 private final PatientRepository patients; private final DoctorRepository doctors; private final DepartmentRepository departments; private final AppointmentRepository appointments; private final RoomRepository rooms;
 public ReportController(PatientRepository p,DoctorRepository d,DepartmentRepository dep,AppointmentRepository a,RoomRepository r){patients=p;doctors=d;departments=dep;appointments=a;rooms=r;}
 @GetMapping("/dashboard") public Map<String,Object> dashboard(){Map<String,Object> m=new LinkedHashMap<>();m.put("totalPatients",patients.count());m.put("totalDoctors",doctors.count());m.put("totalDepartments",departments.count());m.put("totalAppointments",appointments.count());m.put("availableBeds",rooms.findAll().stream().mapToInt(x->Optional.ofNullable(x.getAvailableBeds()).orElse(0)).sum());m.put("todayAppointments",appointments.findByAppointmentDate(java.time.LocalDate.now()).size());m.put("recentPatients",patients.findAll().stream().limit(5).map(this::patientDto).toList());m.put("todayAppointmentList",appointments.findByAppointmentDate(java.time.LocalDate.now()).stream().map(this::appointmentDto).toList());return m;}
 @GetMapping("/doctor-statistics") public Map<String,Long> doctorStats(){return patients.findAll().stream().collect(java.util.stream.Collectors.groupingBy(p->Optional.ofNullable(p.getAssignedDoctor()).orElse("Unassigned"),java.util.stream.Collectors.counting()));}
 @GetMapping("/department-statistics") public Map<String,Long> departmentStats(){return doctors.findAll().stream().collect(java.util.stream.Collectors.groupingBy(d->Optional.ofNullable(d.getDepartmentId()).map(String::valueOf).orElse("Unassigned"),java.util.stream.Collectors.counting()));}
 private PatientDTO patientDto(Patient p){PatientDTO d=new PatientDTO();BeanUtils.copyProperties(p,d);return d;}
 private AppointmentDTO appointmentDto(Appointment a){AppointmentDTO d=new AppointmentDTO();BeanUtils.copyProperties(a,d);return d;}
}
