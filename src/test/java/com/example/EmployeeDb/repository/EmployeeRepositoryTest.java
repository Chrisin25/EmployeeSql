package com.example.EmployeeDb.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.EmployeeDb.models.Employee;

@SpringBootTest
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testFindAllById_Found(){

        Employee employee=new Employee("27", "Anu", "Associate", "anu123@aster.com", "sales", "1234567890", "Kochi", "1",LocalDateTime.parse("2013-07-23T14:57"));
        employeeRepository.save(employee);

        Employee found=employeeRepository.findAllById(employee.getId());

        assertEquals(employee.getId(), found.getId());
        assertEquals(employee.getName(), found.getName());
        
    }

    @Test
    void testFindAllById_NotFound(){

        Employee found=employeeRepository.findAllById("55");
        
        assertEquals(null,found);
        
        
    }

    @Test
    void testFindManagers_Found(){
        Employee employee=new Employee("111", "Anu", "Account Manager", "anu123@aster.com", "engineering", "1234567890", "Kochi", "0",LocalDateTime.parse("2013-07-23T14:57"));
        employeeRepository.save(employee);
        List<Employee> found=employeeRepository.findManagers();
        assertThat(found).hasSize(3);
    }

}
