package com.hospital.entity;

import jakarta.persistence.*;
import java.time.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="room_allocations")
public class RoomAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer allocationId;
    @NotNull @Positive
    private Integer patientId;
    @NotNull @Positive
    private Integer roomId;
    @NotNull @Positive
    private Integer bedNumber;
    @NotNull
    private java.time.LocalDate admissionDate;
    private java.time.LocalDate releaseDate;

    public RoomAllocation() {}
    public Integer getAllocationId() { return allocationId; }
    public void setAllocationId(Integer allocationId) { this.allocationId = allocationId; }
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }
    public Integer getBedNumber() { return bedNumber; }
    public void setBedNumber(Integer bedNumber) { this.bedNumber = bedNumber; }
    public java.time.LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(java.time.LocalDate admissionDate) { this.admissionDate = admissionDate; }
    public java.time.LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(java.time.LocalDate releaseDate) { this.releaseDate = releaseDate; }
}