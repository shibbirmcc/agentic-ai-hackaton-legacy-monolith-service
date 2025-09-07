package com.legacy.monolith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legacy.monolith.entity.User;
import com.legacy.monolith.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    
    private MockMvc mockMvc;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private UserController userController;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    
    @Test
    public void testCreateUser() throws Exception {
        User user = new User("testuser", "test@example.com", "Test User");
        user.setId(1L);
        
        when(userService.createUser(any(User.class))).thenReturn(user);
        
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));
    }
    
    @Test
    public void testGetUserById() throws Exception {
        User user = new User("testuser", "test@example.com", "Test User");
        user.setId(1L);
        
        when(userService.getUserById(1L)).thenReturn(user);
        
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }
    
    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(
            new User("user1", "user1@example.com", "User One"),
            new User("user2", "user2@example.com", "User Two")
        );
        
        when(userService.getAllUsers()).thenReturn(users);
        
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }
}