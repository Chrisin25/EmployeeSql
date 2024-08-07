package com.example.EmployeeDb;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.example.EmployeeDb.models.EmployeeResponse;
import com.example.EmployeeDb.models.EmployeeResponseGet;
import com.example.EmployeeDb.models.Input;
import com.example.EmployeeDb.repository.EmployeeRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/")
@EnableMongoRepositories
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepo;
    @PostMapping("/AddEmployee")
    ResponseEntity <Map<String,String>> addEmployeesService(@Valid @RequestBody Employee employee){

        Map<String,String> result=new HashMap<>();
        
        //calculate year of experience
        employee.setYearOfExperience(Period.between(employee.getDateOfJoining().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears());
        //check designation (account manager/associate)
        if(!(employee.getDesignation().matches("Account Manager") || employee.getDesignation().matches("Associate"))){
            result.put("message ","Invalid Designation");
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
        //email validation
        //check department(sales/delivery/qa/engineering/ba)
        if(!(employee.getDepartment().matches("sales") || employee.getDepartment().matches("delivery") || employee.getDepartment().matches("QA") || employee.getDepartment().matches("engineering") || employee.getDepartment().matches("ba"))){
            result.put("message ","Invalid Department");
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
        //mobile validation
        //manager id validation
         if(Integer.parseInt(employee.getId())<=0 && employee.getDesignation()!="Account Manager"){
            result.put("message ","Invalid Id");
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
        
        //add to db
        Date today=new Date();
        employee.setCreatedTime(today);
        employeeRepo.save(employee);
        //print success message
        result.put("message ","successfully created");
        return ResponseEntity.ok(result);//new ResponseEntity<>(result,HttpStatus.OK);
    }


    @PutMapping("/update")
    ResponseEntity <Map<String,String>> UpdateService(@RequestBody Input input){

        Map<String,String> result=new HashMap<>();
        try{
            //find employee using id
            Employee e= employeeRepo.findEmployee(input.getEmployeeId());
            //System.out.println(input.getEmployeeId());
            //System.out.println(e);
            String old=e.getManagerId();
            //System.out.println(old);
            //get previous manager 
            Employee m1=employeeRepo.findEmployee(old);
            //System.out.println(m1);
            e.setManagerId(input.getManagerId());
            Date today=new Date();
            e.setUpdatedTime(today);
            employeeRepo.save(e);
            result.put("message ",""+ e.getName()+"'s manager has been successfully changed from "+m1.getName()+" to "+employeeRepo.findEmployee(input.getManagerId()).getName()+".");
            

        }
        catch(IllegalArgumentException i){
            //System.out.println(i+" specify id");
            result.put("message ","enter id");
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
        catch(NullPointerException n){
            System.out.println(n);
            result.put("message ","manager not found");
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    
    
    @GetMapping("/ViewEmployee")
    public ResponseEntity<EmployeeResponseGet> getFilteredEmployees(
    @RequestParam(value = "year-of-experience", required = false) Integer yearOfExperience,
    @RequestParam(value = "managerId", required = false) String managerId) {
 
    // Retrieve all employees
    List<Employee> employees = employeeRepo.findAll();
    //System.out.println(employees);
    // Group employees by manager ID
    Map<String, List<Employee>> employeesByManager = employees.stream()
        .collect(Collectors.groupingBy(Employee::getManagerId));
    //System.out.println(employeesByManager);
    // Process the employees to filter and map to the response type
    List<EmployeeResponse> filteredResponses = employeesByManager.entrySet().stream()
        .map(entry -> {
            String currentManagerId = entry.getKey();
            //System.out.println("manager id"+currentManagerId);
            List<Employee> employeeList = entry.getValue();
            //System.out.println(employeeList);
            // get manager details
            Optional<Employee> managerOpt = employeeRepo.findById(currentManagerId);
            //System.out.println("manager"+managerOpt);
            String managerNameOpt = managerOpt.map(Employee::getName).orElse("Unknown");
            String managerDept = managerOpt.map(Employee::getDepartment).orElse("Unknown");
            //System.out.println("manager details: "+managerNameOpt+managerDept);
            // Filter employees based on provided criteria
            List<Employee> filteredEmployeeList = employeeList.stream()
                .filter(employee -> {
                    // Calculate years of experience
                    if (employee.getDateOfJoining() != null) {
                        try {
                            // Check the filtering conditions
                            return (managerId == null || managerId.equalsIgnoreCase(currentManagerId)) &&
                                   (yearOfExperience == null || employee.getYearOfExperience() >= yearOfExperience);
                        } catch (Exception e) {
                            // Handle parsing exceptions
                            return false;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
                //System.out.println("filtered"+filteredEmployeeList);
 
            if (!filteredEmployeeList.isEmpty() && Integer.parseInt(currentManagerId) > 0) {
                return new EmployeeResponse(
                    managerNameOpt,  // Set manager's name
                    managerDept,     // Set manager's department
                    currentManagerId, // Set manager ID
                    filteredEmployeeList  // Set filtered list of employees
                );
            } else {
                return null; // Skip empty lists
            }
        })
        .filter(Objects::nonNull) // Filter out null values
        .collect(Collectors.toList());
        //System.out.println(filteredResponses);
    // Create the response message
    String responseMessage = filteredResponses.isEmpty() ? "No employees found" : "Employees retrieved successfully";
 
    // Wrap the result in EmployeeResponseGet
    EmployeeResponseGet response = new EmployeeResponseGet(responseMessage, filteredResponses);
    System.out.println(response);
    // Return the wrapped result with ResponseEntity
    return ResponseEntity.ok(response);
}
    @DeleteMapping("/remove")
    ResponseEntity <Map<String,String>> DeleteEmployeeService(@RequestParam String id) {
        System.out.println(id);
        Map<String,String> result=new HashMap<>();
        Employee e= employeeRepo.findEmployee(id);
        System.out.println(e);
        if(e.getDesignation().matches("Account Manager")){
            //check whether employees exists under manager
            if(employeeRepo.employeesUnderManager(id)==null){
                result.put("message","Successfully deleted "+e.getName()+" from employee list of the organization");
                employeeRepo.deleteById(id); 
            }
            else{
                result.put("message","Cannot delete employee");
            }
        }
        else{
            result.put("message","Successfully deleted "+e.getName()+" from employee list of the organization");
            employeeRepo.deleteById(id);
            
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    
}
