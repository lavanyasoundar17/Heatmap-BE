package com.example.BE_Heatmap.service;

import com.example.BE_Heatmap.dto.LoginRequest;
import com.example.BE_Heatmap.model.User;
import com.example.BE_Heatmap.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection for UserRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        // Check if a user with the same email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with this email already exists. Please log in.");
        }

        // Hash the password before saving
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));


        return userRepository.save(user);
    }


    public User loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()); // Fetch user by email
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            // Compare the raw password with the hashed password
            if (encoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return user; // Return the user if password matches
            }
        }
        return null; // Return null if user is not found or password doesn't match
    }
}
