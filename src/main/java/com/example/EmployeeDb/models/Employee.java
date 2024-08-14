package com.example.EmployeeDb.models;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;


@Document("Employee")
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime dateOfJoining;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String managerId;
    private int yearOfExperience; 

    
    public Employee(String id, String name, String designation, String email, String department,
            String mobile, String location, String managerId,LocalDateTime dateOfJoining) {
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
    public LocalDateTime getDateOfJoining() {
        return dateOfJoining;
    }
    public void setDateOfJoining(LocalDateTime dateOfJoining) {
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
    public int getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(int yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }



}
