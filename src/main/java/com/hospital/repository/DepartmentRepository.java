package com.hospital.repository;
import com.hospital.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByDepartmentNameIgnoreCase(String departmentName);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select d from Department d where d.departmentId=:id") Optional<Department> lockById(@Param("id") Integer id);
}
