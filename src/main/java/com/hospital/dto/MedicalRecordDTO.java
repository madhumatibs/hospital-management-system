package com.hospital.dto;
import jakarta.validation.constraints.*;

public class MedicalRecordDTO {
    private Integer recordId;
    @NotNull @Positive private Integer patientId;
    @NotNull @Positive private Integer doctorId;
    @NotBlank private String diagnosis;
    @NotBlank private String treatment;
    private String medicalNotes;
    @NotNull private java.time.LocalDate recordDate;
    public MedicalRecordDTO() {}
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public String getMedicalNotes() { return medicalNotes; }
    public void setMedicalNotes(String medicalNotes) { this.medicalNotes = medicalNotes; }
    public java.time.LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(java.time.LocalDate recordDate) { this.recordDate = recordDate; }
}
