package com.example.EmployeeDb.models;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
public class Employee {
    
    private String name;
    @Id
    private String id;
    @Pattern(regexp = "Account Manager|Associate",message = "invalid designation")
    private String designation;
    @Pattern(regexp="sales|delivery|QA|engineering|BA",message = "invalid department")
    private String department;
    @Email(message = "Email is not valid")
    private String email;
    @NotBlank(message = "mobileNumber is required")
    @Size(min = 10, max = 10,message = "mobile number should contain exactly 10 digits")
    private String mobile;
    
    private String location;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime dateOfJoining;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime updatedTime;
    
    private String managerId;
    
    private long yearOfExperience; 

    
    public Employee(String id, String name, String designation, String email, String department,
            String mobile, String location, String managerId,OffsetDateTime dateOfJoining) {
        this.id = id;
        this.name = name;
        this.dateOfJoining = dateOfJoining;
        this.designation = designation;
        this.email = email;
        this.department = department;
        this.mobile = mobile;
        this.location = location;
        this.managerId = managerId;
    }

    public Employee() {
    }

   

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public OffsetDateTime getDateOfJoining() {
        return dateOfJoining;
    }
    public void setDateOfJoining(OffsetDateTime dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String emailId) {
        this.email = emailId;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getManagerId() {
        return managerId;
    }
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    public long getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(long yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }
    public OffsetDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(OffsetDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public OffsetDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(OffsetDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }



}
