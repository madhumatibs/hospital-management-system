package com.hospital.service.impl;
import com.hospital.entity.Department; import com.hospital.repository.DepartmentRepository; import com.hospital.repository.DoctorRepository; import org.junit.jupiter.api.Test; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.InjectMocks; import org.mockito.Mock; import org.mockito.junit.jupiter.MockitoExtension; import java.util.Optional; import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class) class DepartmentServiceImplTest {
 @Mock DepartmentRepository departments; @Mock DoctorRepository doctors; @InjectMocks DepartmentServiceImpl service;
 @Test void createsUniqueDepartment(){Department d=new Department();d.setDepartmentName("Cardiology");when(departments.findByDepartmentNameIgnoreCase("Cardiology")).thenReturn(Optional.empty());when(departments.save(d)).thenReturn(d);assertEquals(d,service.create(d));}
 @Test void rejectsDuplicateDepartmentName(){Department d=new Department();d.setDepartmentName("Cardiology");Department existing=new Department();existing.setDepartmentId(3);when(departments.findByDepartmentNameIgnoreCase("Cardiology")).thenReturn(Optional.of(existing));assertThrows(IllegalArgumentException.class,()->service.create(d));}
 @Test void preventsDeletingDepartmentWithDoctors(){when(departments.lockById(1)).thenReturn(java.util.Optional.of(new com.hospital.entity.Department()));when(doctors.countByDepartmentId(1)).thenReturn(1L);assertThrows(IllegalArgumentException.class,()->service.delete(1));}
}
