package com.example.EmployeeDb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.EmployeeDb.repository.EmployeeRepository;
import com.example.EmployeeDb.repository.projection.EmployeeProjection;
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
@Test
public void testDeleteEmployeeService_NotFound() {
    when(employeeRepository.findById(toString())).thenReturn(Optional.empty());

    ResponseEntity<Map<String, String>> response = employeeService.DeleteEmployeeService("1");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Employee doesnot exist", response.getBody().get("message"));
}
@Test
public void testDeleteEmployeeService_ManagerWithSubordinates() {
    // Arrange
    String managerId = "1";
    Employee manager = new Employee(managerId , "John Doe", "Account Manager","john@aspire.com","BA","1234567890","Kochi","0",LocalDateTime.now());
    EmployeeProjection subordinateProjection = mock(EmployeeProjection.class); // Create a mock projection
    List<EmployeeProjection> subordinates = Collections.singletonList(subordinateProjection);
    when(employeeRepository.findAllById(managerId)).thenReturn(manager);
    when(employeeRepository.findAllByManagerId(managerId)).thenReturn(subordinates); // Return the mock projection list

    // Act
    ResponseEntity<Map<String, String>> response = employeeService.DeleteEmployeeService(managerId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Cannot delete manager", response.getBody().get("message"));
    verify(employeeRepository, never()).deleteById(toString());
    
    }
    @Test
    public void testGetEmployeeWithNullManagerId() {
        // Arrange
        Integer yearOfExperience = 5;
        String managerId = null;
        List<Employee> managers = Arrays.asList(new Employee(managerId , "John Doe", "Account Manager","john@aspire.com","BA","1234567890","Kochi","0",LocalDateTime.now()));
        when(employeeRepository.findManagers()).thenReturn(managers);
        when(employeeRepository.findAllByManagerId("1")).thenReturn(Arrays.asList(/* employees */));

        // Act
        Map<String, Object> result = employeeService.getemployeecontroller(yearOfExperience, managerId);

        // Assert
        assertNotNull(result);
        assertEquals("succesfuly fetched", result.get("message"));
        // Add more assertions here
    }
    @Test
    public void testGetEmployeeWithNullYearOfExperience() {
        // Arrange
        Integer yearOfExperience = null;
        String managerId = "1";
        List<Employee> managers = Arrays.asList(new Employee(managerId , "John Doe", "Account Manager","john@aspire.com","BA","1234567890","Kochi","0",LocalDateTime.now()));
        when(employeeRepository.findManagers()).thenReturn(managers);
        when(employeeRepository.findAllByManagerId("1")).thenReturn(Arrays.asList(/* employees */));

        // Act
        Map<String, Object> result = employeeService.getemployeecontroller(yearOfExperience, managerId);

        // Assert
        assertNotNull(result);
        assertEquals("succesfuly fetched", result.get("message"));
        // Add more assertions here
    }
    @Test
    public void testGetEmployeeWithNullManagerAndYearOfExperience() {
        // Arrange
        Integer yearOfExperience = null;
        String managerId = null;
        List<Employee> managers = Arrays.asList(new Employee("1" , "John Doe", "Account Manager","john@aspire.com","BA","1234567890","Kochi","0",LocalDateTime.now()));
        when(employeeRepository.findManagers()).thenReturn(managers);
        when(employeeRepository.findAllByManagerId("1")).thenReturn(Arrays.asList(/* employees */));

        // Act
        Map<String, Object> result = employeeService.getemployeecontroller(yearOfExperience, managerId);

        // Assert
        assertNotNull(result);
        assertEquals("succesfuly fetched", result.get("message"));
        // Add more assertions here
    }
    @Test
    public void testGetEmployeeWithManagerIdAndYearOfExperience() {
        // Arrange
        Integer yearOfExperience = 2;
        String managerId = "1";
        List<Employee> managers = Arrays.asList(new Employee(managerId , "John Doe", "Account Manager","john@aspire.com","BA","1234567890","Kochi","0",LocalDateTime.now()));
        
        when(employeeRepository.findManagers()).thenReturn(managers);
        
        // Act
        Map<String, Object> result = employeeService.getemployeecontroller(yearOfExperience, managerId);

        // Assert
        assertNotNull(result);
        assertEquals("succesfuly fetched", result.get("message"));
        // Add more assertions here
    }
    @Test
public void testUpdateService_Successful() {
    // Arrange
    Map<String, String> employeeId = new HashMap<>();
    employeeId.put("employeeId", "1");
    employeeId.put("managerId", "3");

    Employee mockEmployee = new Employee();
    mockEmployee.setId("1");
    mockEmployee.setName("John Doe");
    mockEmployee.setManagerId("2");
    mockEmployee.setDepartment("sales");

    Employee mockoldManager = new Employee();
    mockoldManager.setId("2");
    mockoldManager.setName("James");
    mockoldManager.setDepartment("sales");

    Employee mockNewManager = new Employee();
    mockNewManager.setId("2");
    mockNewManager.setName("Jo");
    mockNewManager.setDepartment("BA");

    when(employeeRepository.findAllById(employeeId.get("employeeId"))).thenReturn(mockEmployee);
    when(employeeRepository.findAllById("2")).thenReturn(mockoldManager);
    when(employeeRepository.findAllById("3")).thenReturn(mockNewManager);

    // Act
    ResponseEntity<Map<String, String>> response = employeeService.UpdateService(employeeId);
    

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("John Doe's manager has been successfully changed from"));
}

@Test
public void testUpdateService_EmployeeNotFound() {
    // Arrange
    Map<String, String> employeeId = new HashMap<>();
    employeeId.put("employeeId", "invalidEmployeeId");

    when(employeeRepository.findAllById("invalidEmployeeId")).thenThrow(new NullPointerException());

    ResponseEntity<Map<String, String>> response=employeeService.UpdateService(employeeId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("Employee not found"));
}

@Test
public void testUpdateService_ManagerNotFound() {
    // Arrange
    Map<String, String> employeeId = new HashMap<>();
    employeeId.put("employeeId", "validEmployeeId");
    employeeId.put("managerId", "invalidManagerId");

    Employee mockEmployee = new Employee();
    mockEmployee.setName("John Doe");
    mockEmployee.setManagerId("oldManagerId");

    when(employeeRepository.findAllById("validEmployeeId")).thenReturn(mockEmployee);
    when(employeeRepository.findAllById("invalidManagerId")).thenThrow(new NullPointerException());

    ResponseEntity<Map<String, String>> response=employeeService.UpdateService(employeeId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("Manager not found"));
}
@Test
public void testAddService_Success(){
    Employee employee = new Employee("1" , "John Doe", "Account Manager","john@aspire.com","BA","1234567890","Kochi","0",LocalDateTime.now());
    ResponseEntity<Map<String, String>> response=employeeService.addEmployeesService(employee);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("successfully created"));

}
@Test
public void testAddManagerService_ManagerExist(){
    Employee employee = new Employee("1" , "John Doe", "Account Manager","john@aspire.com","sales","1234567890","Kochi","0",LocalDateTime.now());
    Employee employee2 = new Employee("2" , "Jo Doe", "Account Manager","john@aspire.com","sales","1234567890","Kochi","0",LocalDateTime.now());
    when(employeeRepository.findAllByDesignationAndDepartment("Account Manager", "sales")).thenReturn(Arrays.asList(employee));
    ResponseEntity<Map<String, String>> response=employeeService.addEmployeesService(employee2);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("Manager already exist"));

}
@Test
public void testAddAssociateService_DepartmentNotExist(){
    Employee employee = new Employee("1" , "John Doe", "Associate","john@aspire.com","BA","1234567890","Kochi","0",LocalDateTime.now());
    //Employee employee2 = new Employee("2" , "Jo Doe", "Account Manager","john@aspire.com","sales","1234567890","Kochi","0",LocalDateTime.now());
    when(employeeRepository.findAllByDepartment( "BA")).thenReturn(List.of());
    ResponseEntity<Map<String, String>> response=employeeService.addEmployeesService(employee);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("Department doesnot exist"));

}


}


