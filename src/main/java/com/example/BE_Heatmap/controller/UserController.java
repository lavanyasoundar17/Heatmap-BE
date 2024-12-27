package com.example.BE_Heatmap.controller;

import com.example.BE_Heatmap.dto.LoginRequest;
import com.example.BE_Heatmap.model.User;
import com.example.BE_Heatmap.service.UserService;
import com.example.BE_Heatmap.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestBody;

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
    public ResponseEntity<?> loginUser(
            @RequestBody LoginRequest payload
            //@RequestParam String email, @RequestParam String password
    ) {
        User loggedInUser = userService.loginUser(payload);

        if (loggedInUser != null) {
            String token = jwtUtil.generateToken(loggedInUser.getId());
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

    // 5. Upload profile image
//    @PostMapping("/upload-profile-img")
//    public ResponseEntity<?> uploadProfileImage(@RequestHeader("Authorization") String bearerToken, @RequestParam("file") MultipartFile file) {
//        String token = bearerToken.split(" ")[1];
//        String userId = jwtUtil.extractUserId(token);
//        String filePath = userService.uploadProfileImage(userId, file);
//        return ResponseEntity.ok("Profile image uploaded successfully to " + filePath);
//    }
//
//    @GetMapping("/profile/image")
//    public ResponseEntity<?> getProfileImage(@RequestHeader("Authorization") String bearerToken) {
//        String token = bearerToken.split(" ")[1];
//        String userId = jwtUtil.extractUserId(token);
//        try {
//            File profileImg = userService.getProfileImage(userId);
//            byte[] fileContent = Files.readAllBytes(profileImg.toPath());
//            return ResponseEntity.ok(fileContent);
//        }catch (Exception e) {
//            return ResponseEntity.status(401).body("file not found");
//        }
//
//    }
}
