package com.example.EmployeeDb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.EmployeeDb.models.Employee;
import com.example.EmployeeDb.repository.projection.EmployeeProjection;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee,String>{

    public Employee findAllById(String id);

    @Query("{designation:'Account Manager'}")
    public List<Employee> findManagers();

    @Query(value="{'managerId':?0}",fields="{'name':1,'id':1,'designation':1,'department':1,'email':1,'mobile':1,'location':1,'dateOfJoining':1,'createdTime':1,'updatedTime':1}")
    public List<EmployeeProjection> findAllByManagerId(String id);
    @Query(fields="{'name':1,'id':1,'designation':1,'department':1,'email':1,'mobile':1,'location':1,'dateOfJoining':1,'createdTime':1,'updatedTime':1}")
    public List<EmployeeProjection> findAllByManagerIdAndYearOfExperienceGreaterThanEqual(String id, Integer yearOfExperience);

    public List<Employee> findAllByDesignationAndDepartment(String string, String department);

    public List<Employee> findAllByDepartment(String department);
}
