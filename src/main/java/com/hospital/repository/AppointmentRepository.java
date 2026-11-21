package com.hospital.repository;
import com.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByDoctorId(Integer doctorId);
    List<Appointment> findByPatientId(Integer patientId);
    List<Appointment> findByAppointmentDate(LocalDate date);
    long countByPatientId(Integer patientId);
    long countByDoctorId(Integer doctorId);
    @Query("select a from Appointment a where a.doctorId=:doctorId and a.appointmentDate=:date and a.appointmentTime=:time and lower(coalesce(a.status,'')) not in ('cancelled','canceled','completed') and (:excludeId is null or a.appointmentId<>:excludeId)")
    List<Appointment> findDoctorSlotConflicts(@Param("doctorId") Integer doctorId, @Param("date") LocalDate date, @Param("time") LocalTime time, @Param("excludeId") Integer excludeId);
    @Query("select a from Appointment a where a.patientId=:patientId and a.appointmentDate=:date and a.appointmentTime=:time and lower(coalesce(a.status,'')) not in ('cancelled','canceled','completed') and (:excludeId is null or a.appointmentId<>:excludeId)")
    List<Appointment> findPatientSlotConflicts(@Param("patientId") Integer patientId, @Param("date") LocalDate date, @Param("time") LocalTime time, @Param("excludeId") Integer excludeId);
}
