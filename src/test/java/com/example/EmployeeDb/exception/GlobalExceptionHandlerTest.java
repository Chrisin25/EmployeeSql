package com.example.EmployeeDb.exception;

import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.EmployeeDb.controller.EmployeeController;
import com.example.EmployeeDb.service.EmployeeService;

@WebMvcTest
public class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeController employeeController;
    @MockBean
    private EmployeeService employeeService;

   
    @Test
    public void testHandleMethodArgumentNotValidException() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("designation", "developer");

        mockMvc.perform(post("/AddEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"designation\":\"developer\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.designation").exists());
    }
    
}
