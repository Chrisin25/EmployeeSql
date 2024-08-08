package com.example.EmployeeDb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.EmployeeDb.models.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee,String>{

    public Employee findAllById(String id);

    @Query("{designation:'Account Manager'}")
    public List<Employee> findManagers();

    public List<Employee> findAllByManagerId(String id);

    //public List<Employee> findAllByManagerIdAndYearOfExperienceGreater(String id, Integer yearOfExperience);

    public List<Employee> findAllByManagerIdAndYearOfExperienceGreaterThanEqual(String id, Integer yearOfExperience);
}
