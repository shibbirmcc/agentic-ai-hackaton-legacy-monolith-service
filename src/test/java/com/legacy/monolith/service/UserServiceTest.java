package com.legacy.monolith.service;

import com.legacy.monolith.entity.User;
import com.legacy.monolith.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ValidationService validationService;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    public void testCreateUser() {
        User user = new User("newuser", "new@example.com", "New User");
        
        when(userRepository.findByUsername("newuser")).thenReturn(null);
        when(userRepository.findByEmail("new@example.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        User result = userService.createUser(user);
        
        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    public void testCreateUserWithExistingUsername() {
        assertThrows(RuntimeException.class, () -> {
        User existingUser = new User("existinguser", "existing@example.com", "Existing User");
        User newUser = new User("existinguser", "new@example.com", "New User");
        
        when(userRepository.findByUsername("existinguser")).thenReturn(existingUser);
        
        userService.createUser(newUser);
        });
    }
    
    @Test
    public void testGetUserById() {
        User user = new User("testuser", "test@example.com", "Test User");
        user.setId(1L);
        
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        
        User result = userService.getUserById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
    }
    
    @Test
    public void testGetActiveUsers() {
        List<User> activeUsers = Arrays.asList(
            new User("user1", "user1@example.com", "User One"),
            new User("user2", "user2@example.com", "User Two")
        );
        
        when(userRepository.findByStatus("ACTIVE")).thenReturn(activeUsers);
        
        List<User> result = userService.getActiveUsers();
        
        assertEquals(2, result.size());
    }
    
    @Test
    public void testValidateUserData() {
        User validUser = new User("validuser", "valid@example.com", "Valid User");
        assertTrue(userService.validateUserData(validUser));
        
        User invalidUser = new User("ab", "invalid", "Invalid User");
        assertFalse(userService.validateUserData(invalidUser));
    }
}