package com.example.EmployeeDb.repository.projection;

import java.util.Date;

public interface EmployeeProjection {
    String getName();
    String getId();
    String getDesignation();
    String getDepartment();
    String getEmail();
    String getMobile();
    String getLocation();
    Date getDateOfJoining();
    Date getCreatedTime();
    Date getUpdatedTime();
    
}
