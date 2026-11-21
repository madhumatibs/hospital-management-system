package com.hospital.repository;
import com.hospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findBySpecializationIgnoreCase(String specialization);
    List<Doctor> findByDepartmentId(Integer departmentId);
    Optional<Doctor> findByEmailIgnoreCase(String email);
    long countByDepartmentId(Integer departmentId);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select d from Doctor d where d.doctorId=:id") Optional<Doctor> lockById(@Param("id") Integer id);
}
