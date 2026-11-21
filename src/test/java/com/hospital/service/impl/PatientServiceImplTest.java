package com.hospital.service.impl;
import com.hospital.entity.Patient; import com.hospital.repository.*; import org.junit.jupiter.api.Test; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.InjectMocks; import org.mockito.Mock; import org.mockito.junit.jupiter.MockitoExtension; import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class) class PatientServiceImplTest {
 @Mock PatientRepository patients; @Mock AppointmentRepository appointments; @Mock MedicalRecordRepository records; @Mock RoomAllocationRepository allocations; @InjectMocks PatientServiceImpl service;
 private Patient valid(){Patient p=new Patient();p.setPatientName("Sample Patient");p.setAge(32);p.setGender("Other");p.setAdmissionDate(java.time.LocalDate.now());p.setAilment("Checkup");p.setAssignedDoctor("Dr Test");return p;}
 @Test void createsValidPatient(){Patient p=valid();when(patients.save(p)).thenReturn(p);assertEquals(p,service.create(p));}
 @Test void rejectsOutOfRangeAge(){Patient p=valid();p.setAge(150);assertThrows(IllegalArgumentException.class,()->service.create(p));}
 @Test void preventsDeletingPatientWithAppointments(){when(patients.lockById(1)).thenReturn(java.util.Optional.of(new com.hospital.entity.Patient()));when(appointments.countByPatientId(1)).thenReturn(1L);assertThrows(IllegalArgumentException.class,()->service.delete(1));}
}
