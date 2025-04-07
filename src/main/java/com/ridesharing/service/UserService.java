package com.ridesharing.service;

import com.ridesharing.model.User;
import com.ridesharing.model.UserRole;
import com.ridesharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // In a real app, use Spring Security for password encoding and authentication
    public User registerUser(String username, String email, String password, UserRole role) {
        if (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Username or Email already exists"); // Use custom exceptions
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password); // Hash the password here!
        newUser.setRole(role);
        return userRepository.save(newUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Add login, profile update methods etc.
} 