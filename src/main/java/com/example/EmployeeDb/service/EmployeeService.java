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
    public ResponseEntity<Map<String,String>> DeleteEmployeeService(String employeeId) {
        Map<String,String> result=new HashMap<>();
        try{
        Employee employee= employeeRepository.findAllById(employeeId);//throws exception when employee not found
        if(employee.getDesignation().matches("Account Manager")){
            //delete manager without subordinates 
            if(employeeRepository.findAllByManagerId(employeeId).isEmpty()){
                result.put("message","Successfully deleted "+employee.getName()+" from employee list of the organization");
                
                    employeeRepository.deleteById(employeeId); 
                
                
            }
            else{
                result.put("message","Cannot delete manager");
            }
        }
        else{
            result.put("message","Successfully deleted "+employee.getName()+" from employee list of the organization");
            
                employeeRepository.deleteById(employeeId); 
                return new ResponseEntity<>(result,HttpStatus.OK);
            
            
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    catch(NullPointerException n){
        result.put("message","Requested employee id cannot be found");
        return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
        
    }
   
    }
   //GET
   public ResponseEntity<Map<String,Object>> getEmployeeService(Long yearOfExperience,String newManagerId){
    Map<String,Object> result=new LinkedHashMap<>();
    List<Object> detailList=new ArrayList<>();
    //create a list of managers
    List<Employee> managerList=employeeRepository.findByDesignation("Account Manager");
    for(Employee manager:managerList){
         //if manager id is not given add all managers to map 
         //if manager id is given add only that manager to map
        if(newManagerId==null || newManagerId.matches(manager.getId()) ){
        Map<String,Object> employeeManagerMap=new LinkedHashMap<>();
        employeeManagerMap.put("accountManager", manager.getName());
        employeeManagerMap.put("departement", manager.getDepartment());
        employeeManagerMap.put("employeeId",manager.getId());
        //list of employees under the manager
        try{
        List<EmployeeDTO> employeeList;
        //only manager id is given
        if(yearOfExperience==null){
           
            employeeList=getEmployeesByManagerId(manager.getId());
        }
        //manager id and year of experience given
        else{
            employeeList=getEmployeesByManagerIdAndYearOfExperience(manager.getId(),yearOfExperience);
        }
        //employees are present satisfying the given conditons employee list is added to Map
        if(!employeeList.isEmpty()){
            employeeManagerMap.put("employeeList",employeeList);
            detailList.add(employeeManagerMap); 
        }
        
        }
        catch(Exception e2){
            System.out.println("new exception found  :   "+e2);
        }
        
        }
        
    }
    if(detailList.isEmpty()){
        result.put("message","no employees can be found satisfying this condition");
        return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
    }
    else{
        result.put("message","succesfuly fetched");
        result.put("details", detailList);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    
}

//UPDATE
public ResponseEntity <Map<String,String>> UpdateService(Map<String,String> employeeUpdateMap){
    
    Map<String,String> result=new HashMap<>();
    if(employeeUpdateMap.get("employeeId")==null){
        result.put("message ","specify employeeId of the employee to be updated");
        return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
    }
    if(employeeUpdateMap.get("managerId")==null){
        result.put("message ","specify new manager Id for updation");
        return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
    }
    try{
        //find employee using employeeId
        Employee employee= employeeRepository.findAllById(employeeUpdateMap.get("employeeId"));
        if(employee==null){
            result.put("message ","Employee not found");
        return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
        }
        try{ 
        String oldManagerId=employee.getManagerId();
        if(oldManagerId.matches(employeeUpdateMap.get("managerId"))){
            result.put("message ","Employee is already under the given manager");
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
        //get previous manager 
        Employee previousManager=employeeRepository.findAllById(oldManagerId);
        Employee newManager=employeeRepository.findByIdAndDesignation(employeeUpdateMap.get("managerId"),"Account Manager");
        employee.setManagerId(employeeUpdateMap.get("managerId"));
        employee.setDepartment(newManager.getDepartment());
        employee.setUpdatedTime(OffsetDateTime.now());
        employeeRepository.save(employee);
        result.put("message ",""+ employee.getName()+"'s manager has been successfully changed from "
        +previousManager.getName()+" to "+newManager.getName()+".");
       }
       catch(NullPointerException ne){
        System.out.println(ne);
        result.put("message ","Cannot find any managers with requested manager id");
        return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
        }
    }
    catch(NullPointerException n){
    System.out.println(n);
    result.put("message ","Cannot find any employee with requested employee id");
    return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
    } 
    return new ResponseEntity<>(result,HttpStatus.OK);
}


   
//ADD
private static final AtomicInteger GENERATE_ID=new AtomicInteger(106);
public ResponseEntity <Map<String,String>> addEmployeesService(Employee employee){
    Map<String,String> result=new HashMap<>();
    if(employeeRepository!=null){
        List<Employee> managerList=employeeRepository.findAllByDesignationAndDepartment("Account Manager",employee.getDepartment());
        if(!managerList.isEmpty() && employee.getDesignation().matches("Account Manager")){
            result.put("message ","Manager already exist in this department");
            return new ResponseEntity<>(result,HttpStatus.CONFLICT);
        }
        List<Employee> employeeList=employeeRepository.findAllByDepartment(employee.getDepartment());
        if(employeeList.isEmpty() && employee.getDesignation().matches("Associate")){
            result.put("message ","Department doesnot exist");
            return new ResponseEntity<>(result,HttpStatus.FAILED_DEPENDENCY);
        }
    }
        Employee managerExist=employeeRepository.findAllById(employee.getManagerId());
        if(managerExist==null && employee.getDesignation().matches("Associate")){
                result.put("message ","Given manager doesnot exist");
                return new ResponseEntity<>(result,HttpStatus.FAILED_DEPENDENCY);
            }
            Employee employeeExist=employeeRepository.findAllByNameAndDesignationAndEmailAndMobile(employee.getName(),employee.getDesignation(),employee.getEmail(),employee.getMobile());
            if(employeeExist!=null){
                result.put("message ","Given employee already  exist");
                return new ResponseEntity<>(result,HttpStatus.CONFLICT);
            }
    try{
        OffsetDateTime dateOfJoin=employee.getDateOfJoining();
            //calculate year of experience
    employee.setYearOfExperience(ChronoUnit.YEARS.between(dateOfJoin, OffsetDateTime.now()));
    }
    catch(Exception e){
        System.out.println("Exception caught:  ");
        System.out.println(e);
        
    }
    
    //Generate employeeId
    int newId=GENERATE_ID.getAndIncrement();
    employee.setId(String.valueOf(newId));
    //add to db
    employee.setCreatedTime(OffsetDateTime.now());
    employee.setUpdatedTime(OffsetDateTime.now());
    
        employeeRepository.save(employee);
        result.put("message ","successfully created");
   
    
    return new ResponseEntity<>(result,HttpStatus.CREATED);

}
public List<EmployeeDTO> getEmployeesByManagerId(String newManagerId) {
    List<Employee> employees = employeeRepository.findAllByManagerId(newManagerId);
    return employees.stream()
            .map(this::convertToDTO)
            .toList();
}
public List<EmployeeDTO> getEmployeesByManagerIdAndYearOfExperience(String newManagerId,Long yearOfExperience) {
    List<Employee> employees = employeeRepository.findAllByManagerIdAndYearOfExperienceGreaterThanEqual(newManagerId, yearOfExperience);
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