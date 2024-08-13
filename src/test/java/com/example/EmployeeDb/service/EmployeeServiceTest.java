package com.example.EmployeeDb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.EmployeeDb.repository.EmployeeRepository;
import com.example.EmployeeDb.models.Employee;

@SpringBootTest
public class EmployeeServiceTest {
    @Mock
    EmployeeRepository employeeRepository;
    @InjectMocks
    EmployeeService employeeService;   
    @Test
public void whenDeleteNonManagerEmployee_thenSuccess() {
    
    String employeeId = "emp123";
    Employee employee = new Employee();
    employee.setId(employeeId);
    employee.setName("Anil");
    employee.setDesignation("Associate");
    employeeRepository.save(employee);
    when(employeeRepository.findAllById(employeeId)).thenReturn(employee);
    ResponseEntity<Map<String, String>> response = employeeService.DeleteEmployeeService(employeeId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Successfully deleted Anil from employee list of the organization", response.getBody().get("message"));
    verify(employeeRepository).deleteById(employeeId);


}
@Test
public void whenDeleteManagerWithoutSubordinates_thenSuccess() {
    
    String employeeId = "emp123";
    Employee employee = new Employee();
    employee.setId(employeeId);
    employee.setName("Anil");
    employee.setDesignation("Account Manager");
    employee.setDepartment("BA");
    employeeRepository.save(employee);
    when(employeeRepository.findAllById(employeeId)).thenReturn(employee);
    ResponseEntity<Map<String, String>> response = employeeService.DeleteEmployeeService(employeeId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Successfully deleted Anil from employee list of the organization", response.getBody().get("message"));
    verify(employeeRepository).deleteById(employeeId);

}



}
