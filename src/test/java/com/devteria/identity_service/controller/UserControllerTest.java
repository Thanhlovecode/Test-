package com.devteria.identity_service.controller;

import com.devteria.identity_service.config.Configuration;
import com.devteria.identity_service.dto.request.UserRequest;
import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.exceptions.UserExistedException;
import com.devteria.identity_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(UserController.class)
@Import(Configuration.class)
class UserControllerTest {

     @Autowired
     MockMvc mockMvc;

     @Autowired
     ObjectMapper objectMapper;

     @MockitoBean
     UserService userService;


    private static final String END_POINT_PATH = "/api/v1/users";

    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        LocalDate dob = LocalDate.of(1990, 1, 1);
        userRequest = UserRequest.builder()
                .username("john")
                .firstname("john")
                .lastname("doe")
                .password("123456")
                .dob(dob)
                .build();
    }

    @Test
    void testValid_WhenUsernameInvalid_Return400AndException() throws Exception {
        userRequest.setUsername("jo");
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Username must be at least 3 characters"))
                .andDo(print());
    }

    @Test
    void testValid_WhenPasswordInvalid_Return400AndException() throws Exception {
        userRequest.setPassword("123");
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Password must be at least 5 characters"))
                .andDo(print());
    }

    @Test
    void testValid_WhenDateOfBirthInvalid_Return400AndException() throws Exception {
        userRequest.setDob(LocalDate.of(2023, 1, 2));
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("your age must be at least 18 ages"))
                .andDo(print());
    }

    @Test
    void testCreate_WhenUsernameAlreadyExists_ReturnException() throws Exception {
        Mockito.doThrow(new UserExistedException(ErrorCode.USER_EXISTED.getMessage()))
                .when(userService).createUser(ArgumentMatchers.any());
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("User existed"))
                .andDo(print());
    }

    @Test
    void testCreateUser_Success_Return201() throws Exception {
        Mockito.doNothing().when(userService).createUser(ArgumentMatchers.any());
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
    }
}
