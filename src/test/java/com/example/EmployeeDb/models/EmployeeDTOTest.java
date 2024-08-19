package com.example.EmployeeDb.models;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

public class EmployeeDTOTest {
    @Test
    void testGetCreatedTime() {
        EmployeeDTO e=new EmployeeDTO();
        OffsetDateTime date=OffsetDateTime.now();
        e.setCreatedTime(date);
        assertEquals(date,e.getCreatedTime());
    }

    @Test
    void testGetDateOfJoining() {
        EmployeeDTO e=new EmployeeDTO();
        OffsetDateTime date=OffsetDateTime.now();
        e.setDateOfJoining(date);
        assertEquals(date,e.getDateOfJoining());
    }

    @Test
    void testGetDepartment() {
        EmployeeDTO e=new EmployeeDTO();
        e.setDepartment("sales");
        assertEquals("sales",e.getDepartment());
    }

    @Test
    void testGetDesignation() {
        EmployeeDTO e=new EmployeeDTO();
        e.setDesignation("Associate");;
        assertEquals("Associate",e.getDesignation());
    }

    @Test
    void testGetEmail() {
        EmployeeDTO e=new EmployeeDTO();
        e.setEmail("abc@aspire.com");
        assertEquals("abc@aspire.com",e.getEmail());
    }

    
    @Test
    void testGetLocation() {
        EmployeeDTO e=new EmployeeDTO();
        e.setLocation("kochi");
        assertEquals("kochi",e.getLocation());
    }

    @Test
    void testGetMobile() {
        EmployeeDTO e=new EmployeeDTO();
        e.setMobile("1234567890");
        assertEquals("1234567890",e.getMobile());
    }

    @Test
    void testGetName() {
        EmployeeDTO e=new EmployeeDTO();
        e.setName("Jo");
        assertEquals("Jo",e.getName());
    }

    @Test
    void testGetUpdatedTime(){
        EmployeeDTO e=new EmployeeDTO();
        OffsetDateTime date=OffsetDateTime.now();
        e.setUpdatedTime(date);
        assertEquals(date,e.getUpdatedTime());
    }
}

