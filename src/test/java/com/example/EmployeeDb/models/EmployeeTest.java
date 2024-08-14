package com.example.EmployeeDb.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class EmployeeTest {
    @Test
    public void TestgetEmail(){
        Employee e=new Employee();
        e.setEmail("abc@aspire.com");
        assertEquals("abc@aspire.com",e.getEmail());
    }
    @Test
    public void TestgetMobile(){
        Employee e=new Employee();
        e.setMobile("1234567890");
        assertEquals("1234567890",e.getMobile());
    }
    @Test
    public void TestgetLocation(){
        Employee e=new Employee();
        e.setLocation("Kochi");
        assertEquals("Kochi",e.getLocation());
    }
    @Test
    public void TestgetYearOfExperience(){
        Employee e=new Employee();
        e.setYearOfExperience(5);
        assertEquals(5, e.getYearOfExperience());
    }
    @Test
    public void TestgetCreatedTime(){
        Employee e=new Employee();
        LocalDateTime dateNow=LocalDateTime.now();
        e.setCreatedTime(dateNow);
        assertEquals(dateNow, e.getCreatedTime());
    }
    @Test
    public void TestgetUpdatedTime(){
        Employee e=new Employee();
        LocalDateTime dateNow=LocalDateTime.now();
        e.setUpdatedTime(dateNow);
        assertEquals(dateNow, e.getUpdatedTime());
    }
}
