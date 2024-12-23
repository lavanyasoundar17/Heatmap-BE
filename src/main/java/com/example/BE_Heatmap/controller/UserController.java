package com.example.BE_Heatmap.controller;

import com.example.BE_Heatmap.model.User;
import com.example.BE_Heatmap.service.UserService;
import com.example.BE_Heatmap.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;


    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 1. Register a new user
    @PostMapping("/reg")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User createdUser = userService.registerUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // 2. User login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        User loggedInUser = userService.loginUser(email, password);

        if (loggedInUser != null) {
            String token = jwtUtil.generateToken(loggedInUser.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("id", loggedInUser.getId());
            response.put("name", loggedInUser.getName());
            response.put("email", loggedInUser.getEmail());
            response.put("profileImageUrl", loggedInUser.getProfileImageUrl());
            response.put("token", token);  // Add JWT token

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    // 3. Delete user by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // 4. Update user profile
    @PutMapping("/profile-update/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable String id, @RequestBody User updatedUser) {
        User updated = userService.updateUser(id, updatedUser);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    // 5. Upload profile image
    @PostMapping("/profile-img/{id}")
    public ResponseEntity<?> uploadProfileImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        String filePath = userService.uploadProfileImage(id, file);
        return ResponseEntity.ok("Profile image uploaded successfully to " + filePath);
    }
}
