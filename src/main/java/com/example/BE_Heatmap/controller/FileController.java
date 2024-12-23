package com.example.BE_Heatmap.controller;

import com.example.BE_Heatmap.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    // Constructor-based injection of FileService
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "overwrite", required = false, defaultValue = "false") boolean overwrite,
                                        @AuthenticationPrincipal Jwt jwt) {
        try {
            // Extract the userId (subject) from the token
            String userId = jwt.getClaim("sub");
            String fileName = file.getOriginalFilename();

            // Check if the file already exists for the user
            if (fileService.getFileUrl(userId, fileName).isPresent() && !overwrite) {
                return ResponseEntity.status(409).body("File already exists. Use overwrite=true to replace it.");
            }
            String fileUrl = fileService.uploadFile(file, userId, overwrite);

            return ResponseEntity.ok("File uploaded successfully! File URL: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
        }
    }

}
