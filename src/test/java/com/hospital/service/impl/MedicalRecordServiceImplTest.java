
package com.hospital.service.impl;

import com.hospital.entity.*;
import com.hospital.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceImplTest {

    @Mock
    MedicalRecordRepository records;

    @Mock
    PatientRepository patients;

    @Mock
    DoctorRepository doctors;

    @InjectMocks
    MedicalRecordServiceImpl service;

    @Test
    void savesRecordWhenReferencesExist() {

        MedicalRecord r = new MedicalRecord();
        r.setPatientId(1);
        r.setDoctorId(2);
        r.setRecordDate(java.time.LocalDate.now());

        when(doctors.lockById(2))
                .thenReturn(java.util.Optional.of(new Doctor()));

        when(patients.lockById(1))
                .thenReturn(java.util.Optional.of(new Patient()));

        when(records.save(r)).thenReturn(r);

        assertEquals(r, service.create(r));
    }

    @Test
    void rejectsMissingPatient() {

        MedicalRecord r = new MedicalRecord();
        r.setPatientId(99);
        r.setDoctorId(2);
        r.setRecordDate(java.time.LocalDate.now());

        when(doctors.lockById(2))
                .thenReturn(java.util.Optional.of(new Doctor()));

        when(patients.lockById(99))
                .thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.create(r));
    }
}
