package com.example.BE_Heatmap.service;

import com.example.BE_Heatmap.model.User;
import com.example.BE_Heatmap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

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


    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email); // Fetch user by email
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            // Compare the raw password with the hashed password
            if (encoder.matches(password, user.getPassword())) {
                return user; // Return the user if password matches
            }
        }
        return null; // Return null if user is not found or password doesn't match
    }

    public void deleteUser(String id) {
        // Find the user in the database
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.getProfileImageUrl() != null) {
                String userDirectory = uploadDir + "/" + id;
                deleteDirectory(new File(userDirectory));
            }
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    // Helper method to delete a directory and its contents
    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    public User updateUser(String id, User updatedUser) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            // Update fields
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());

            // Hash password before saving it
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(updatedUser.getPassword());
            user.setPassword(hashedPassword);

            user.setProfileImageUrl(updatedUser.getProfileImageUrl());
            return userRepository.save(user);
        }
        return null;
    }
    // Logic to handle profile image upload
    public String uploadProfileImage(String userId, MultipartFile file) {
        Path uploadPath = Paths.get(uploadDir, userId).toAbsolutePath();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Error creating directory for file upload", e);
        }

        // Get the original filename
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("File must have a valid name.");
        }
        Path filePath = uploadPath.resolve(originalFilename);

        try {
            // Transfer the file to the target location
            file.transferTo(filePath.toFile());
            // Get the file URL or path to store in the database
            String fileUrl = "/uploads/profile_images/" + userId + "/" + originalFilename;
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                user.setProfileImageUrl(fileUrl);
                userRepository.save(user);
            }

            // Return the file URL
            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file", e);
        }
    }
}
