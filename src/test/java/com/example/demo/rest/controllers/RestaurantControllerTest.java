package com.example.demo.rest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getMenu() throws Exception {
        this.mockMvc.perform(get("/restaurant/menu"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{" +
                                "'menus':[]"+
                                "}"
                ));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void addMenu() throws Exception{
        this.mockMvc.perform(post("/restaurant/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Kachori\",\"price\":11.0}")
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        "{\"menus\":[{\"name\":\"Kachori\",\"price\":11.0}],\"maybeOrders\":null}"
                ));
    }
}