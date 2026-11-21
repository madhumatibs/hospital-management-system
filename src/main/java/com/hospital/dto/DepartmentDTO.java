
package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartmentDTO {
    private Integer departmentId;

    @NotBlank
    private String departmentName;

    private String description;
    private String departmentContact;

    public DepartmentDTO() {}

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentContact() {
        return departmentContact;
    }

    public void setDepartmentContact(String departmentContact) {
        this.departmentContact = departmentContact;
    }
}
