package com.legacy.monolith.repository;

import com.legacy.monolith.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

// @RunWith(SpringRunner.class)
// @DataJpaTest
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    // @Test
    public void testSaveAndFindUser() {
        User user = new User("testuser", "test@example.com", "Test User");
        user.setStatus("ACTIVE");
        
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }
    
    // @Test
    public void testFindByUsername() {
        User user = new User("uniqueuser", "unique@example.com", "Unique User");
        userRepository.save(user);
        
        User foundUser = userRepository.findByUsername("uniqueuser");
        assertNotNull(foundUser);
        assertEquals("unique@example.com", foundUser.getEmail());
    }
}