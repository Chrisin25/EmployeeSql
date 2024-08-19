package com.example.EmployeeDb.models;

import java.time.OffsetDateTime;

public class EmployeeDTO {
    
        private String id;
        private String name;
        private String designation;
        private String department;
        private String email;
        private String mobile;
        private String location;
        private OffsetDateTime dateOfJoining;
        private OffsetDateTime createdTime;
        private OffsetDateTime updatedTime;
        public EmployeeDTO(String id, String name, String designation, String department, String email, String mobile,
                String location, OffsetDateTime dateOfJoining, OffsetDateTime createdTime, OffsetDateTime updatedTime) {
            this.id = id;
            this.name = name;
            this.designation = designation;
            this.department = department;
            this.email = email;
            this.mobile = mobile;
            this.location = location;
            this.dateOfJoining = dateOfJoining;
            this.createdTime = createdTime;
            this.updatedTime = updatedTime;
        }
        public EmployeeDTO() {
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
        public String getDesignation() {
            return designation;
        }
        public void setDesignation(String designation) {
            this.designation = designation;
        }
        public String getDepartment() {
            return department;
        }
        public void setDepartment(String department) {
            this.department = department;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
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
        public OffsetDateTime getDateOfJoining() {
            return dateOfJoining;
        }
        public void setDateOfJoining(OffsetDateTime dateOfJoining) {
            this.dateOfJoining = dateOfJoining;
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
