package com.hospital.repository;
import com.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    List<Patient> findByAdmissionDateBetween(LocalDate startDate, LocalDate endDate);
    List<Patient> findByAilmentContainingIgnoreCase(String ailment);
    List<Patient> findByAssignedDoctorIgnoreCase(String doctorName);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select p from Patient p where p.patientId=:id") Optional<Patient> lockById(@Param("id") Integer id);
}
