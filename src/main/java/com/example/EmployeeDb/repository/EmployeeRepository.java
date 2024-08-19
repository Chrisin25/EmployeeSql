package com.example.EmployeeDb.repository;

import java.util.List;

import com.example.EmployeeDb.models.Employee;

public interface EmployeeRepository{

    public Employee findById(String id);
    
    public List<Employee> findByDesignation(String designation);

    public List<Employee> findAllByManagerId(String managerId);
    
    public List<Employee> findAllByManagerIdAndYearOfExperienceGreaterThanEqual(String id, Long yearOfExperience);

    public List<Employee> findAllByDesignationAndDepartment(String string, String department);

    public List<Employee> findAllByDepartment(String department);

    public void deleteById(String id);

    public void save(Employee e);
}
