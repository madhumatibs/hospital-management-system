package com.hospital.entity;

import jakarta.persistence.*;
import java.time.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;
    @NotNull @Positive
    private Integer patientId;
    @NotNull @Positive
    private Integer doctorId;
    @NotNull
    private java.time.LocalDate appointmentDate;
    @NotNull
    private java.time.LocalTime appointmentTime;
    private String reason;
    private String status;

    public Appointment() {}
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
    public java.time.LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(java.time.LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public java.time.LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(java.time.LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}