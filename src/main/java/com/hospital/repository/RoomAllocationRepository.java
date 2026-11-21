package com.hospital.repository;
import com.hospital.entity.RoomAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
public interface RoomAllocationRepository extends JpaRepository<RoomAllocation, Integer> {
    List<RoomAllocation> findByPatientId(Integer patientId);
    List<RoomAllocation> findByRoomId(Integer roomId);
    long countByRoomIdAndReleaseDateIsNull(Integer roomId);
    long countByPatientId(Integer patientId);
    long countByRoomId(Integer roomId);
    long countByPatientIdAndReleaseDateIsNull(Integer patientId);
    boolean existsByRoomIdAndBedNumberAndReleaseDateIsNull(Integer roomId, Integer bedNumber);
    boolean existsByPatientIdAndReleaseDateIsNull(Integer patientId);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select a from RoomAllocation a where a.allocationId=:id") Optional<RoomAllocation> lockById(@Param("id") Integer id);
}
