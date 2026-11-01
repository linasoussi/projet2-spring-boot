package com.esb.projet2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Tests des codes HTTP de l'API REST (200, 201, 400, 404). */
@SpringBootTest
@AutoConfigureMockMvc
class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllDepartments_returns200() throws Exception {
        mockMvc.perform(get("/api/departments")).andExpect(status().isOk());
    }

    @Test
    void getUnknownStudent_returns404() throws Exception {
        mockMvc.perform(get("/api/students/999999")).andExpect(status().isNotFound());
    }

    @Test
    void createInvalidStudent_returns400() throws Exception {
        String body = "{\"name\":\"\",\"email\":\"pas-un-email\",\"age\":5}";
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDepartment_returns201() throws Exception {
        String body = "{\"name\":\"Departement-Test-Controller\"}";
        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void invalidStatusInUrl_returns400() throws Exception {
        mockMvc.perform(get("/api/students/status/XYZ")).andExpect(status().isBadRequest());
    }
}
