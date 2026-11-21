package com.hospital.entity;

import jakarta.persistence.*;
import java.time.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;
    @NotBlank
    private String patientName;
    @NotNull @Min(0) @Max(120)
    private Integer age;
    @NotBlank
    private String gender;
    @NotNull
    private java.time.LocalDate admissionDate;
    @NotBlank
    private String ailment;
    @NotBlank
    private String assignedDoctor;

    public Patient() {}
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public java.time.LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(java.time.LocalDate admissionDate) { this.admissionDate = admissionDate; }
    public String getAilment() { return ailment; }
    public void setAilment(String ailment) { this.ailment = ailment; }
    public String getAssignedDoctor() { return assignedDoctor; }
    public void setAssignedDoctor(String assignedDoctor) { this.assignedDoctor = assignedDoctor; }
}