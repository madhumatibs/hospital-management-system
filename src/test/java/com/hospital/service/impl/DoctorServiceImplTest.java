package com.hospital.service.impl;
import com.hospital.entity.Doctor; import com.hospital.repository.*; import org.junit.jupiter.api.Test; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.InjectMocks; import org.mockito.Mock; import org.mockito.junit.jupiter.MockitoExtension; import java.util.Optional; import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class) class DoctorServiceImplTest {
 @Mock DoctorRepository doctors; @Mock DepartmentRepository departments; @Mock AppointmentRepository appointments; @Mock MedicalRecordRepository records; @InjectMocks DoctorServiceImpl service;
 @Test void createsDoctorWithoutDepartment(){Doctor d=new Doctor();d.setDoctorName("Dr Test");d.setEmail("test@example.com");when(doctors.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.empty());when(doctors.save(d)).thenReturn(d);assertEquals(d,service.create(d));}
 @Test void rejectsUnknownDepartment(){Doctor d=new Doctor();d.setDoctorName("Dr Test");d.setDepartmentId(100);when(departments.lockById(100)).thenReturn(Optional.empty());assertThrows(RuntimeException.class,()->service.create(d));}
 @Test void preventsDeletingDoctorWithRecords(){when(doctors.lockById(7)).thenReturn(java.util.Optional.of(new com.hospital.entity.Doctor()));when(appointments.countByDoctorId(7)).thenReturn(0L);when(records.countByDoctorId(7)).thenReturn(2L);assertThrows(IllegalArgumentException.class,()->service.delete(7));}
}
