package com.example.EmployeeDb.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.example.EmployeeDb.models.Employee;
import com.example.EmployeeDb.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;
    @Test
    public void testAddEmployee()throws Exception{
        Map<String,String> response=new HashMap<>();
        response.put("message", "Employee added successfully");

        when(employeeService.addEmployeesService(Mockito.any(Employee.class)))
                .thenReturn(ResponseEntity.ok(response));
                mockMvc.perform(post("/AddEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"designation\":\"Associate\",\"email\":\"john@aspire.com\",\"department\":\"BA\",\"mobile\":\"1234567890\",\"location\":\"Kochi\",\"managerId\":\"0\",\"dateOfJoining\": \"2023-06-28T12:57:59.447\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee added successfully"));
    }
    @Test
    public void testDeleteEmployeeController() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee deleted successfully");

        when(employeeService.DeleteEmployeeService(Mockito.anyString()))
                .thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(delete("/remove")
                .param("id", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee deleted successfully"));
    }
    @Test
    public void testGetEmployeeController() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("employees", new ArrayList<>());

        when(employeeService.getemployeecontroller(Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(get("/ViewEmployee")
                .param("year-of-experience", "5")
                .param("managerId", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employees").isArray());
    }
    @Test
    public void testUpdateController() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee updated successfully");

        when(employeeService.UpdateService(Mockito.anyMap()))
                .thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(put("/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"employeeId\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee updated successfully"));
    }
    
}
