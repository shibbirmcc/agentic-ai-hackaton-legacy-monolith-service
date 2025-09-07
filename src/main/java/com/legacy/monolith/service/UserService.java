package com.legacy.monolith.service;

import com.legacy.monolith.entity.User;
import com.legacy.monolith.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ValidationService validationService;
    
    public User createUser(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }
        
        if (user.getEmail() != null) {
            User emailUser = userRepository.findByEmail(user.getEmail());
            if (emailUser != null) {
                throw new RuntimeException("Email already in use");
            }
        }
        
        user.setCreatedDate(new Date());
        user.setStatus("ACTIVE");
        
        User savedUser = userRepository.save(user);
        
        System.out.println("User created: " + savedUser.getUsername());
        
        return savedUser;
    }
    
    public User getUserById(Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + id);
        }
        return user;
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);
        
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getFullName() != null) {
            existingUser.setFullName(updatedUser.getFullName());
        }
        if (updatedUser.getStatus() != null) {
            existingUser.setStatus(updatedUser.getStatus());
        }
        
        return userRepository.save(existingUser);
    }
    
    public void deleteUser(Long id) {
        User user = getUserById(id);
        user.setStatus("DELETED");
        userRepository.save(user);
    }
    
    public List<User> getActiveUsers() {
        return userRepository.findByStatus("ACTIVE");
    }
    
    public boolean validateUserData(User user) {
        if (user == null) return false;
        if (user.getUsername() == null || user.getUsername().length() < 3) return false;
        if (user.getEmail() != null && !user.getEmail().contains("@")) return false;
        return true;
    }
    
    public void performUserMaintenance() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                user.setEmail(user.getUsername() + "@legacy.com");
                userRepository.save(user);
            }
        }
    }
}