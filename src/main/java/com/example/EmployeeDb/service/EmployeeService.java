package com.example.EmployeeDb.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.EmployeeDb.models.Employee;
import com.example.EmployeeDb.repository.EmployeeRepository;
import com.example.EmployeeDb.repository.projection.EmployeeProjection;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    //DELETE
    public ResponseEntity<Map<String,String>> DeleteEmployeeService(String id) {
        Map<String,String> result=new HashMap<>();
        try{
        Employee e= employeeRepository.findAllById(id);//throws exception when employee not found
        if(e.getDesignation().matches("Account Manager")){
            
            if(employeeRepository.findAllByManagerId(id).isEmpty()){
                result.put("message","Successfully deleted "+e.getName()+" from employee list of the organization");
                employeeRepository.deleteById(id); 
            }
            else{
                result.put("message","Cannot delete manager");
            }
        }
        else{
            result.put("message","Successfully deleted "+e.getName()+" from employee list of the organization");
            employeeRepository.deleteById(id);
            
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    catch(NullPointerException n){
        result.put("message","Employee doesnot exist");
        return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
        
    }
    }
    //GET
    public Map<String,Object> getemployeecontroller(Integer yearOfExperience,String managerId){
        Map<String,Object> result=new LinkedHashMap<>();
        result.put("message","succesfuly fetched");
        List<Object> detailList=new ArrayList<>();
        //create a list of managers
        List<Employee> managerList=employeeRepository.findManagers();
        for(Employee manager:managerList){
            if(managerId==null || managerId.matches(manager.getId()) ){
            Map<String,Object> employeeManager=new LinkedHashMap<>();
            employeeManager.put("accountManager", manager.getName());
            employeeManager.put("departement", manager.getDepartment());
            employeeManager.put("id",manager.getId());
            //list of employees under the manager
            List<EmployeeProjection> employeeList;
            if(yearOfExperience==null){
                employeeList=employeeRepository.findAllByManagerId(manager.getId());
            }
            else{
                employeeList=employeeRepository.findAllByManagerIdAndYearOfExperienceGreaterThanEqual(manager.getId(),yearOfExperience);
            }
            employeeManager.put("employeeList",employeeList);
            detailList.add(employeeManager); 
            }
        }
        result.put("details", detailList);
        return result;
    }
    //UPDATE
    public ResponseEntity <Map<String,String>> UpdateService(Map<String,String> employeeId){
        
        Map<String,String> result=new HashMap<>();
        try{
            //find employee using id
            Employee e= employeeRepository.findAllById(employeeId.get("employeeId"));
            
           try{ 
            String old=e.getManagerId();
            //get previous manager 
            Employee previousManager=employeeRepository.findAllById(old);
            Employee newManager=employeeRepository.findAllById(employeeId.get("managerId"));
            e.setManagerId(employeeId.get("managerId"));
            e.setDepartment(newManager.getDepartment());
            e.setUpdatedTime(LocalDateTime.now());
            employeeRepository.save(e);
            result.put("message ",""+ e.getName()+"'s manager has been successfully changed from "
            +previousManager.getName()+" to "+newManager.getName()+".");
           }
           
           catch(NullPointerException n){
            System.out.println(n);
            result.put("message ","Manager not found");
            return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
        }

        }
        catch(NullPointerException n){
            System.out.println(n);
            result.put("message ","Employee not found");
            return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    private static final AtomicInteger GENERATE_ID=new AtomicInteger(105);
   
//ADD
public ResponseEntity <Map<String,String>> addEmployeesService(Employee employee){
    Map<String,String> result=new HashMap<>();
    LocalDate dateOfJoin=employee.getDateOfJoining().toLocalDate();
    //calculate year of experience
    employee.setYearOfExperience(Period.between(dateOfJoin, LocalDate.now()).getYears());
    //Generate id
    int newId=GENERATE_ID.getAndIncrement();
    employee.setId(String.valueOf(newId));
    
    List<Employee> managerList=employeeRepository.findAllByDesignationAndDepartment("Account Manager",employee.getDepartment());
    if(!managerList.isEmpty() && employee.getDesignation().matches("Account Manager")){
        result.put("message ","Manager already exist");
        return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
    }
    List<Employee> associateList=employeeRepository.findAllByDepartment(employee.getDepartment());
    if(associateList.isEmpty() && employee.getDesignation().matches("Associate")){
        result.put("message ","Department doesnot exist");
        return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
    }
    //add to db
    employee.setCreatedTime(LocalDateTime.now());
    employee.setUpdatedTime(LocalDateTime.now());
    
        employeeRepository.save(employee);
        result.put("message ","successfully created");
    
    return ResponseEntity.ok(result);
}
}