package com.legacy.monolith.repository;

import com.legacy.monolith.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByUsername(String username);
    
    User findByEmail(String email);
    
    List<User> findByStatus(String status);
    
    @Query(value = "SELECT * FROM users WHERE email LIKE %:email%", nativeQuery = true)
    List<User> findByEmailContainingNative(@Param("email") String email);
    
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:name%")
    List<User> searchByName(@Param("name") String name);
}