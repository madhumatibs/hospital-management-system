package com.hospital.entity;

import jakarta.persistence.*;
import java.time.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;
    @NotBlank
    @Column(name="department_name", unique=true)
    private String departmentName;
    private String description;
    private String departmentContact;

    public Department() {}
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDepartmentContact() { return departmentContact; }
    public void setDepartmentContact(String departmentContact) { this.departmentContact = departmentContact; }
}