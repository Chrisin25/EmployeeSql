package com.example.EmployeeDb.models;

public class Input {
    private String EmployeeId;
    private String ManagerId;

    public Input(String employeeId, String managerId) {
        EmployeeId = employeeId;
        ManagerId = managerId;
    }
    
    public String getEmployeeId() {
        return EmployeeId;
    }
    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }
    public String getManagerId() {
        return ManagerId;
    }
    public void setManagerId(String managerId) {
        ManagerId = managerId;
    }

}
