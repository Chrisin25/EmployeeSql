package com.example.EmployeeDb.service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
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
import com.example.EmployeeDb.models.EmployeeDTO;
import com.example.EmployeeDb.repository.EmployeeRepository;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    //DELETE
    public ResponseEntity<Map<String,String>> DeleteEmployeeService(String id) {
        Map<String,String> result=new HashMap<>();
        try{
        Employee e= employeeRepository.findById(id);//throws exception when employee not found
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
                return new ResponseEntity<>(result,HttpStatus.OK);
            
            
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    catch(NullPointerException n){
        result.put("message","Employee doesnot exist");
        return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
        
    }
   
    }
    //GET
    public Map<String,Object> getemployeecontroller(Long yearOfExperience,String managerId){
        Map<String,Object> result=new LinkedHashMap<>();
        result.put("message","succesfuly fetched");
        List<Object> detailList=new ArrayList<>();
        //create a list of managers
        List<Employee> managerList=employeeRepository.findByDesignation("Account Manager");
        for(Employee manager:managerList){
            if(managerId==null || managerId.matches(manager.getId()) ){
            Map<String,Object> employeeManager=new LinkedHashMap<>();
            employeeManager.put("accountManager", manager.getName());
            employeeManager.put("departement", manager.getDepartment());
            employeeManager.put("id",manager.getId());
            //list of employees under the manager
            
            List<EmployeeDTO> employeeList;
            if(yearOfExperience==null){
               
                employeeList=getEmployeesByManagerId(manager.getId());
            }
            else{
                employeeList=getEmployeesByManagerIdAndYearOfExperience(manager.getId(),yearOfExperience);
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
            Employee e= employeeRepository.findById(employeeId.get("employeeId"));
            
           try{ 
            String old=e.getManagerId();
            //get previous manager 
            Employee previousManager=employeeRepository.findById(old);
            Employee newManager=employeeRepository.findById(employeeId.get("managerId"));
            e.setManagerId(employeeId.get("managerId"));
            e.setDepartment(newManager.getDepartment());
            e.setUpdatedTime(OffsetDateTime.now());
            
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

   
//ADD
private static final AtomicInteger GENERATE_ID=new AtomicInteger(105);
public ResponseEntity <Map<String,String>> addEmployeesService(Employee employee){
    Map<String,String> result=new HashMap<>();
    try{
        OffsetDateTime dateOfJoin=employee.getDateOfJoining();
            //calculate year of experience
    employee.setYearOfExperience(ChronoUnit.YEARS.between(dateOfJoin, OffsetDateTime.now()));
    }
    catch(Exception e){
        System.out.println("Exception caught:  ");
        System.out.println(e);
        
    }
    

    //Generate id
    int newId=GENERATE_ID.getAndIncrement();
    employee.setId(String.valueOf(newId));
    
    if(employeeRepository!=null){
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
    }
    /*else{
        if(!employee.getDesignation().matches("Account Manager")){
            result.put("message ","cannot add employee to this department");
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }*/
    //add to db
    employee.setCreatedTime(OffsetDateTime.now());
    employee.setUpdatedTime(OffsetDateTime.now());
    
        employeeRepository.save(employee);
        result.put("message ","successfully created");
   
    
    return new ResponseEntity<>(result,HttpStatus.CREATED);

}
public List<EmployeeDTO> getEmployeesByManagerId(String managerId) {
    List<Employee> employees = employeeRepository.findAllByManagerId(managerId);
    return employees.stream()
            .map(this::convertToDTO)
            .toList();
}
public List<EmployeeDTO> getEmployeesByManagerIdAndYearOfExperience(String managerId,Long yearOfExperience) {
    List<Employee> employees = employeeRepository.findAllByManagerIdAndYearOfExperienceGreaterThanEqual(managerId, yearOfExperience);
    return employees.stream()
            .map(this::convertToDTO)
            .toList();
}
public EmployeeDTO convertToDTO(Employee employee) {
    return new EmployeeDTO(
            employee.getId(),
            employee.getName(),
            employee.getDesignation(),
            employee.getDepartment(),
            employee.getEmail(),
            employee.getMobile(),
            employee.getLocation(),
            employee.getDateOfJoining(),
            employee.getCreatedTime(),
            employee.getUpdatedTime()
    );
}
}