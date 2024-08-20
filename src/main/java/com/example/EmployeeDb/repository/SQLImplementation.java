package com.example.EmployeeDb.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.EmployeeDb.models.Employee;

@Repository
@Component
public class SQLImplementation implements EmployeeRepository{
    
    @Autowired
    MySqlRepo sqlRepo;
    @Override
    public Employee findById(String id)
    {
        return sqlRepo.findAllById(id);
    }
    @Override
    public List<Employee> findByDesignation(String designation)
    {
        return sqlRepo.findByDesignation(designation);
    }
    @Override
    public List<Employee> findAllByManagerId(String managerId)
    {
        return sqlRepo.findAllByManagerId(managerId);
    }
    @Override
    public List<Employee> findAllByManagerIdAndYearOfExperienceGreaterThanEqual(String id, Long yearOfExperience)
    {
        return sqlRepo.findAllByManagerIdAndYearOfExperienceGreaterThanEqual(id,yearOfExperience);
    }
    @Override
    public List<Employee> findAllByDesignationAndDepartment(String string, String department){
        return sqlRepo.findAllByDesignationAndDepartment(string,department);
    }
    @Override
    public List<Employee> findAllByDepartment(String department){
        return sqlRepo.findAllByDepartment(department);
    }
    @Override
    public void deleteById(String id){
        sqlRepo.deleteById(id);
    }
    public void save(Employee e){
        sqlRepo.save(e);
    }
}
