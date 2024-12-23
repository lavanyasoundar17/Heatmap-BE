package com.example.BE_Heatmap.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "enable.firebase", havingValue = "true", matchIfMissing = true)
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() {
        try {
            String firebaseCredentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            String storageBucket = System.getenv("FIREBASE_STORAGE_BUCKET_NAME");

            // Ensure the environment variable is set
            if (firebaseCredentialsPath == null || firebaseCredentialsPath.isEmpty()) {
                throw new RuntimeException("Environment variable GOOGLE_APPLICATION_CREDENTIALS is not set.");
            }
            if (storageBucket == null || storageBucket.isEmpty()) {
                throw new RuntimeException("Environment variable FIREBASE_STORAGE_BUCKET_NAME is not set.");
            }

            FileInputStream serviceAccount = new FileInputStream(firebaseCredentialsPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(storageBucket)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }
}
