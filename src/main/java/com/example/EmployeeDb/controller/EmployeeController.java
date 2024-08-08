package com.example.EmployeeDb.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.EmployeeDb.models.Employee;
import com.example.EmployeeDb.models.Input;
import com.example.EmployeeDb.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/")
public class EmployeeController {
 
    @Autowired
    EmployeeRepository employeeRepository;
    
    @PostMapping("/AddEmployee")
    ResponseEntity <Map<String,String>> addEmployeesService(@Valid @RequestBody Employee employee){
        Map<String,String> result=new HashMap<>();
        
        //calculate year of experience
        employee.setYearOfExperience(Period.between(employee.getDateOfJoining().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears());
        
        //manager id validation
        if(Integer.parseInt(employee.getId())<=0 && employee.getDesignation()!="Account Manager"){
            result.put("message ","Invalid Id");
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }

        if(employeeRepository.existsById(employee.getId())){
            result.put("message ","Employee id already exist");
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
        
        //add to db
        Date today=new Date();
        employee.setCreatedTime(today);
        //if((employeeRepo.existsById(employee.getManagerId())==true && employee.getDesignation()=="Associate")||
        //employeeRepo.existsById(employee.getManagerId())==false && employee.getDesignation()=="Account Manager"){
            employeeRepository.save(employee);
            result.put("message ","successfully created");
        /* }
        else{
            result.put("message ","Employee cannot be created in this department");
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }*/
        //print success message
        
        return ResponseEntity.ok(result);//new ResponseEntity<>(result,HttpStatus.OK);
    }


    @PutMapping("/update")
    ResponseEntity <Map<String,String>> UpdateService(@RequestBody Input input){
        //change input format

        Map<String,String> result=new HashMap<>();
        try{
            //find employee using id
            Employee e= employeeRepository.findAllById(input.getEmployeeId());
            
           try{ 
            String old=e.getManagerId();
            //get previous manager 
            Employee m1=employeeRepository.findAllById(old);
            Employee m2=employeeRepository.findAllById(input.getManagerId());
            e.setManagerId(input.getManagerId());
            e.setDepartment(m2.getDepartment());
            Date today=new Date();
            e.setUpdatedTime(today);
            employeeRepository.save(e);
            result.put("message ",""+ e.getName()+"'s manager has been successfully changed from "+m1.getName()+" to "+employeeRepository.findAllById(input.getManagerId()).getName()+".");
           }
           
           catch(NullPointerException n){
            System.out.println(n);
            result.put("message ","Manager not found");
            return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
        }

        }
        catch(IllegalArgumentException i){
            result.put("message ","Enter valid id");
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
        catch(NullPointerException n){
            System.out.println(n);
            result.put("message ","Employee not found");
            return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    
    
    @GetMapping("/ViewEmployee")
    public Map<String,Object> getemployeecontroller(
    @RequestParam(value = "year-of-experience", required = false) Integer yearOfExperience,
    @RequestParam(value = "managerId", required = false) String managerId
    ){
        Map<String,Object> result=new LinkedHashMap<>();
        result.put("message","succesfuly fetched");
        System.out.println("result "+result);
        List<Object> detailList=new ArrayList<>();
        List<Employee> managerList=employeeRepository.findManagers();
        System.out.println("managerList "+managerList);
        for(Employee manager:managerList){
            if(managerId==null || managerId.matches(manager.getId()) ){
            
            Map<String,Object> employeeManager=new LinkedHashMap<>();
            employeeManager.put("accountManager", manager.getName());
            employeeManager.put("departement", manager.getDepartment());
            employeeManager.put("id",manager.getId());
            List<Employee> employeeList;
            if(yearOfExperience==null){
                employeeList=employeeRepository.findAllByManagerId(manager.getId());
            }
            else{
                employeeList=employeeRepository.findAllByManagerIdAndYearOfExperienceGreaterThanEqual(manager.getId(),yearOfExperience);
            }
            //employeesUnderManager(manager.getId());//filter
            
            employeeManager.put("employeeList",employeeList);
            System.out.println("employeemanager "+employeeManager);
            detailList.add(employeeManager); 
            }
        }
        System.out.println("detailList"+detailList);
        result.put("details", detailList);
        System.out.println("result "+result);
        return result;
    }
    
    @DeleteMapping("/remove")
    ResponseEntity <Map<String,String>> DeleteEmployeeService(@RequestParam String id) {
        Map<String,String> result=new HashMap<>();
        try{
        Employee e= employeeRepository.findAllById(id);
        if(e.getDesignation().matches("Account Manager")){
            //check whether employees exists under manager
            if(employeeRepository.findAllByManagerId(id)==null){
                result.put("message","Successfully deleted "+e.getName()+" from employee list of the organization");
                employeeRepository.deleteById(id); 
            }
            else{
                result.put("message","Cannot delete employee");
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
    
}
