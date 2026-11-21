package com.hospital.repository;
import com.hospital.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
    java.util.List<MedicalRecord> findByPatientId(Integer patientId);
    java.util.List<MedicalRecord> findByDoctorId(Integer doctorId);
    long countByPatientId(Integer patientId);
    long countByDoctorId(Integer doctorId);
}
