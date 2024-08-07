package com.example.EmployeeDb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.EmployeeDb.models.Employee;



public interface EmployeeRepository extends MongoRepository<Employee,String>{

    @Query("{id:'?0'}")
    public Employee findEmployee(String id);

    @Query("{managerId:'?0'}")
    public List<Employee> employeesUnderManager(String managerId);
}
