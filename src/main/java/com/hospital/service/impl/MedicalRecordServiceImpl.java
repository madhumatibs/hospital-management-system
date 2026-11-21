
package com.hospital.service.impl;

import com.hospital.entity.MedicalRecord;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.MedicalRecordRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.service.MedicalRecordService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicalRecordServiceImpl
        extends AbstractCrudService<MedicalRecord>
        implements MedicalRecordService {

    private final MedicalRecordRepository records;
    private final PatientRepository patients;
    private final DoctorRepository doctors;

    public MedicalRecordServiceImpl(
            MedicalRecordRepository records,
            PatientRepository patients,
            DoctorRepository doctors) {

        super(records);
        this.records = records;
        this.patients = patients;
        this.doctors = doctors;
    }

    @Override
    @Transactional
    public MedicalRecord create(MedicalRecord r) {
        r.setRecordId(null);
        validate(r);
        return records.save(r);
    }

    @Override
    @Transactional
    public MedicalRecord update(Integer id, MedicalRecord r) {
        findById(id);
        validate(r);
        r.setRecordId(id);
        return records.save(r);
    }

    private void validate(MedicalRecord r) {

        if (r.getPatientId() == null) {
            throw new ResourceNotFoundException(
                    "Patient not found: " + r.getPatientId());
        }

        if (r.getDoctorId() == null) {
            throw new ResourceNotFoundException(
                    "Doctor not found: " + r.getDoctorId());
        }

        doctors.lockById(r.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found: " + r.getDoctorId()));

        patients.lockById(r.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found: " + r.getPatientId()));

        if (r.getRecordDate() == null) {
            throw new IllegalArgumentException(
                    "Record date is required.");
        }
    }
}
