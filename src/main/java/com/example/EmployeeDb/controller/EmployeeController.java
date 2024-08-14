package com.example.EmployeeDb.controller;

import java.util.Map;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.EmployeeDb.models.Employee;
import com.example.EmployeeDb.service.EmployeeService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/")
public class EmployeeController {
 
    @Autowired
    EmployeeService employeeService;
    
    @PostMapping("/AddEmployee")
    ResponseEntity <Map<String,String>> addEmployeesController(@Valid @RequestBody Employee employee){
        return employeeService.addEmployeesService(employee);
    }


    @PutMapping("/update")
    ResponseEntity <Map<String,String>> UpdateController(@RequestBody Map<String,String> employeeId){
        return employeeService.UpdateService(employeeId);
       
    }
    
    @GetMapping("/ViewEmployee")
    public Map<String,Object> getemployeecontroller(
    @RequestParam(value = "year-of-experience", required = false) Integer yearOfExperience,
    @RequestParam(value = "managerId", required = false) String managerId
    ){
        return employeeService.getemployeecontroller(yearOfExperience, managerId);
    }
    
    @DeleteMapping("/remove")
    ResponseEntity <Map<String,String>> DeleteEmployeeController(@RequestParam String id) {
        return employeeService.DeleteEmployeeService(id);
        
    }   
    
}
