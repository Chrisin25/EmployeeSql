package com.example.EmployeeDb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.EmployeeDb.repository.EmployeeRepository;
import com.example.EmployeeDb.models.Employee;
import com.example.EmployeeDb.models.EmployeeDTO;

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
    when(employeeRepository.findById(anyString())).thenThrow(new NullPointerException());
        ResponseEntity<Map<String, String>> response = employeeService.DeleteEmployeeService("111");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("message", "Requested employee id cannot be found");
}
 
@Test
public void testDeleteEmployeeService_ManagerWithSubordinates() {
    
    String managerId = "1";
    Employee manager = new Employee(managerId , "John Doe", "Account Manager","john@aspire.com","BA","1234567890","Kochi","0",OffsetDateTime.now());
    Employee subordinate = mock(Employee.class); // Create a mock projection
    List<Employee> subordinates = Collections.singletonList(subordinate);
    when(employeeRepository.findAllById(managerId)).thenReturn(manager);
    when(employeeRepository.findAllByManagerId(managerId)).thenReturn(subordinates); // Return the mock projection list
    ResponseEntity<Map<String, String>> response = employeeService.DeleteEmployeeService(managerId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Cannot delete manager", response.getBody().get("message"));
    verify(employeeRepository, never()).deleteById(toString());
    
    }
    @Test
    public void testGetEmployeeWithNullManagerId() {
        
        long yearOfExperience = 5;
        String managerId = null;
        List<Employee> managers = Arrays.asList(new Employee("5" , "John Doe", "Account Manager","john@aspire.com","BA","1234567890","Kochi","0",OffsetDateTime.now()));
        List<Employee> employeeList=Arrays.asList(new Employee("123" , "Jo", "Associate","jo@aspire.com","BA","1234567890","Kochi","1",OffsetDateTime.now()));
        when(employeeRepository.findByDesignation("Account Manager")).thenReturn(managers);
        when(employeeRepository.findAllByManagerIdAndYearOfExperienceGreaterThanEqual("1",0L)).thenReturn(employeeList);
        Map<String, Object> result = employeeService.getEmployeeService(yearOfExperience, managerId).getBody();
        assertNotNull(result);
        
    }
    @Test
    public void testGetEmployeeWithNullYearOfExperience() {
       
        Long yearOfExperience = null;
        String managerId = "1";
        List<Employee> managers = Arrays.asList(new Employee(managerId , "John Doe", "Account Manager","john@aspire.com","BA","1234567890","Kochi","0",OffsetDateTime.now()));
        List<Employee> employeeList=Arrays.asList(new Employee(managerId , "Jo", "Associate","jo@aspire.com","BA","1234567890","Kochi","1",OffsetDateTime.now()));
        when(employeeRepository.findByDesignation("Account Manager")).thenReturn(managers);
        when(employeeRepository.findAllByManagerId("1")).thenReturn(employeeList);
        Map<String, Object> result = employeeService.getEmployeeService(yearOfExperience, managerId).getBody();
        assertNotNull(result);
        assertEquals("succesfuly fetched", result.get("message"));
        
    } 
    @Test
    public void testGetEmployeeWithNullManagerAndYearOfExperience() {
       
        Long yearOfExperience = null;
        String managerId = null;
        List<Employee> managers = Arrays.asList(new Employee("1" , "John Doe", "Account Manager","john@aspire.com","BA","1234567890","Kochi","0",OffsetDateTime.now()));
        List<Employee> employeeList=Arrays.asList(new Employee(managerId , "Jo", "Associate","jo@aspire.com","BA","1234567890","Kochi","1",OffsetDateTime.now()));
        when(employeeRepository.findByDesignation("Account Manager")).thenReturn(managers);
        when(employeeRepository.findAllByManagerId("1")).thenReturn(employeeList);
        Map<String, Object> result = employeeService.getEmployeeService(yearOfExperience, managerId).getBody();
        assertNotNull(result);
        assertEquals("succesfuly fetched", result.get("message"));
        
    }
    @Test
    public void testGetEmployeeController_WithManagerIdAndYearOfExperience() {
        String managerId = "manager123";
        Long yearOfExperience = 0L;
        Employee manager = new Employee();
        manager.setId(managerId);
        manager.setName("John Doe");
        manager.setDepartment("Sales");
        Employee employee1 = new Employee("emp1","jane","Associate","jane@aspire.com","sales","1234567890","kochi","manager123",OffsetDateTime.now());
        List<Employee> managerList = Arrays.asList(manager);
        List<Employee> employeeList = Arrays.asList(employee1);
        when(employeeRepository.findByDesignation("Account Manager")).thenReturn(managerList);
        when(employeeRepository.findAllByManagerIdAndYearOfExperienceGreaterThanEqual(managerId, yearOfExperience)).thenReturn(employeeList);
        ResponseEntity<Map<String, Object>> response = employeeService.getEmployeeService(yearOfExperience, managerId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("details"));
        
    }

    @Test
public void testUpdateService_Successful() {
    
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
    when(employeeRepository.findByIdAndDesignation("3","Account Manager")).thenReturn(mockNewManager);
    ResponseEntity<Map<String, String>> response = employeeService.UpdateService(employeeId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("John Doe's manager has been successfully changed from"));
}

@Test
public void testUpdateService_EmployeeNotFound() {
    Map<String, String> employeeId = new HashMap<>();
    employeeId.put("employeeId", "1");
    employeeId.put("managerId", "1");
    when(employeeRepository.findAllById("1")).thenReturn(null);
    ResponseEntity<Map<String, String>> response=employeeService.UpdateService(employeeId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("Employee not found"));
}

@Test
public void testUpdateService_ManagerNotFound() {
   
    Map<String, String> employeeId = new HashMap<>();
    employeeId.put("employeeId", "validEmployeeId");
    employeeId.put("managerId", "invalidManagerId");
    Employee mockEmployee = new Employee();
    mockEmployee.setName("John Doe");
    mockEmployee.setManagerId("oldManagerId");
    when(employeeRepository.findAllById("validEmployeeId")).thenReturn(mockEmployee);
    when(employeeRepository.findById("invalidManagerId")).thenThrow(new NullPointerException());
    ResponseEntity<Map<String, String>> response=employeeService.UpdateService(employeeId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("Cannot find any managers with requested manager id"));
}
@Test
public void testAddService_Success(){
    Employee employee = new Employee("1" , "John Doe", "Account Manager","john@aspire.com","Engineering","1234567890","Kochi","0",OffsetDateTime.now());
    ResponseEntity<Map<String, String>> response=employeeService.addEmployeesService(employee);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("successfully created"));

}
@Test
public void testAddService_managerNotExist(){
    Employee employee = new Employee("1" , "John Doe", "Associate","john@aspire.com","QA","1234567890","Kochi","5",OffsetDateTime.now());
    when(employeeRepository.findAllById(employee.getManagerId())).thenReturn(null);
    List<Employee> empList=Arrays.asList(employee);
    when(employeeRepository.findAllByDepartment(employee.getDepartment())).thenReturn(empList);
    ResponseEntity<Map<String, String>> response=employeeService.addEmployeesService(employee);
    assertEquals(HttpStatus.FAILED_DEPENDENCY, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("Given manager doesnot exist"));

}
@Test
public void testAddService_alreadyPresent(){
    Employee employee = new Employee("1" , "John Doe", "Account Manager","john@aspire.com","Engineering","1234567890","Kochi","0",OffsetDateTime.now());
    when(employeeRepository.findAllByNameAndDesignationAndEmailAndMobile(employee.getName(),employee.getDesignation(),employee.getEmail(),employee.getMobile())).thenReturn(employee);
    ResponseEntity<Map<String, String>> response=employeeService.addEmployeesService(employee);
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("Given employee already  exist"));

}
@Test
public void testAddManagerService_ManagerExist(){
    Employee employee = new Employee("1" , "John Doe", "Account Manager","john@aspire.com","sales","1234567890","Kochi","0",OffsetDateTime.now());
    Employee employee2 = new Employee("2" , "Jo Doe", "Account Manager","john@aspire.com","sales","1234567890","Kochi","0",OffsetDateTime.now());
    when(employeeRepository.findAllByDesignationAndDepartment("Account Manager", "sales")).thenReturn(Arrays.asList(employee));
    ResponseEntity<Map<String, String>> response=employeeService.addEmployeesService(employee2);
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("Manager already exist"));

}
@Test
public void testAddAssociateService_DepartmentNotExist(){
    Employee employee = new Employee("1" , "John Doe", "Associate","john@aspire.com","BA","1234567890","Kochi","0",OffsetDateTime.now());
    when(employeeRepository.findAllByDepartment( "BA")).thenReturn(List.of());
    ResponseEntity<Map<String, String>> response=employeeService.addEmployeesService(employee);
    assertEquals(HttpStatus.FAILED_DEPENDENCY, response.getStatusCode());
    assertTrue(response.getBody().containsKey("message "));
    assertTrue(response.getBody().get("message ").contains("Department doesnot exist"));

}
@Test
public void test_getEmployeesByManagerId(){
    OffsetDateTime date = OffsetDateTime.now();
    Employee e = new Employee("1", "John Doe", "Associate", "john@aspire.com", "sales", "1234567890", "Kochi", "5", date);
    e.setCreatedTime(date);
    e.setUpdatedTime(date);
    List<Employee> employeeList = Arrays.asList(e);
    List<EmployeeDTO> employeeDTOList = Arrays.asList(new EmployeeDTO("1", "John Doe", "Associate", "sales", "john@aspire.com", "1234567890", "Kochi", date, date, date));
    when(employeeRepository.findAllByManagerId("5")).thenReturn(employeeList);
    assertEquals(employeeDTOList.size(), employeeService.getEmployeesByManagerId("5").size());
    assertEquals(employeeDTOList.get(0).getId(), employeeService.getEmployeesByManagerId("5").get(0).getId());


}
@Test
public void test_getEmployeesByManagerIdAndYearOfExperience(){
    OffsetDateTime date = OffsetDateTime.now();
    Employee e = new Employee("1", "John Doe", "Associate", "john@aspire.com", "sales", "1234567890", "Kochi", "5", date);
    e.setCreatedTime(date);
    e.setUpdatedTime(date);
    e.setYearOfExperience(0L);
    List<Employee> employeeList = Arrays.asList(e);
    List<EmployeeDTO> employeeDTOList = Arrays.asList(new EmployeeDTO("1", "John Doe", "Associate", "sales", "john@aspire.com", "1234567890", "Kochi", date, date, date));
    when(employeeRepository.findAllByManagerIdAndYearOfExperienceGreaterThanEqual("5", 0L)).thenReturn(employeeList);
    List<EmployeeDTO> result = employeeService.getEmployeesByManagerIdAndYearOfExperience("5", 0L);
    assertEquals(employeeDTOList.size(), result.size());
    assertEquals(employeeDTOList.get(0).getId(), result.get(0).getId());
}
@Test
    public void test_convertToDTO() {
        OffsetDateTime date = OffsetDateTime.now();
        Employee employee = new Employee("1", "John Doe", "Account Manager", "john@aspire.com", "sales", "1234567890", "Kochi", "5", date);
        employee.setCreatedTime(date);
        employee.setUpdatedTime(date);
        EmployeeDTO expectedDTO = new EmployeeDTO(
                "1",
                "John Doe",
                "Account Manager",
                "sales",
                "john@aspire.com",
                "1234567890",
                "Kochi",
                date,
                date,
                date
        );
        EmployeeDTO actualDTO = employeeService.convertToDTO(employee);
        assertEquals(expectedDTO.getId(), actualDTO.getId());
    }
}
