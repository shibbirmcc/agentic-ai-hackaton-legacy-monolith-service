package com.legacy.monolith.service;

import com.legacy.monolith.entity.User;
import com.legacy.monolith.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceEdgeCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUserWithNullUsername() {
        User user = new User();
        user.setUsername(null);
        user.setEmail("test@example.com");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("Username is required", exception.getMessage());
    }

    @Test
    public void testCreateUserWithEmptyUsername() {
        User user = new User();
        user.setUsername("");
        user.setEmail("test@example.com");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("Username is required", exception.getMessage());
    }

    @Test
    public void testCreateUserWithDuplicateUsername() {
        User existingUser = new User("testuser", "existing@example.com", "Existing User");
        when(userRepository.findByUsername("testuser")).thenReturn(existingUser);

        User newUser = new User("testuser", "new@example.com", "New User");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(newUser);
        });
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    public void testCreateUserWithDuplicateEmail() {
        User existingUser = new User("existing", "test@example.com", "Existing User");
        when(userRepository.findByUsername("newuser")).thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(existingUser);

        User newUser = new User("newuser", "test@example.com", "New User");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(newUser);
        });
        assertEquals("Email already in use", exception.getMessage());
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(999L);
        });
        assertEquals("User not found with id: 999", exception.getMessage());
    }

    @Test
    public void testUpdateUserPartialUpdate() {
        User existingUser = new User("testuser", "old@example.com", "Old Name");
        existingUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updateData = new User();
        updateData.setEmail("new@example.com");
        // Don't set fullName or status

        User updatedUser = userService.updateUser(1L, updateData);

        assertEquals("new@example.com", existingUser.getEmail());
        assertEquals("Old Name", existingUser.getFullName()); // Should remain unchanged
        verify(userRepository).save(existingUser);
    }

    @Test
    public void testValidateUserDataWithNullUser() {
        boolean result = userService.validateUserData(null);
        assertFalse(result);
    }

    @Test
    public void testValidateUserDataWithShortUsername() {
        User user = new User("ab", "test@example.com", "Test User");
        boolean result = userService.validateUserData(user);
        assertFalse(result);
    }

    @Test
    public void testValidateUserDataWithInvalidEmail() {
        User user = new User("testuser", "invalid-email", "Test User");
        boolean result = userService.validateUserData(user);
        assertFalse(result);
    }

    @Test
    public void testValidateUserDataWithValidData() {
        User user = new User("testuser", "test@example.com", "Test User");
        boolean result = userService.validateUserData(user);
        assertTrue(result);
    }

    @Test
    public void testValidateUserDataWithNullEmail() {
        User user = new User("testuser", null, "Test User");
        boolean result = userService.validateUserData(user);
        assertTrue(result); // Null email should be valid
    }

    @Test
    public void testPerformUserMaintenance() {
        User userWithoutEmail = new User("user1", null, "User One");
        User userWithEmptyEmail = new User("user2", "", "User Two");
        User userWithEmail = new User("user3", "user3@example.com", "User Three");

        when(userRepository.findAll()).thenReturn(Arrays.asList(
            userWithoutEmail, userWithEmptyEmail, userWithEmail
        ));

        userService.performUserMaintenance();

        verify(userRepository, times(2)).save(any(User.class));
        assertEquals("user1@legacy.com", userWithoutEmail.getEmail());
        assertEquals("user2@legacy.com", userWithEmptyEmail.getEmail());
        assertEquals("user3@example.com", userWithEmail.getEmail()); // Should remain unchanged
    }
}
