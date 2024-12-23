package com.example.BE_Heatmap.service;

import com.example.BE_Heatmap.model.FileMetadata;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final Firestore firestore;
    private static final String FILE_METADATA_COLLECTION = "file_metadata";

    // Constructor-based injection of Firestore instance
    public FileService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    // Method to upload file to Firebase Storage and store metadata in Firestore
    public String uploadFile(MultipartFile file, String userId, boolean overwrite) throws IOException {
        String fileName = file.getOriginalFilename();

        // Check if the file already exists in Firebase Storage
        Blob existingBlob = StorageClient.getInstance().bucket().get(userId + "/" + fileName);
        if (existingBlob != null && !overwrite) {
            throw new IllegalStateException("File with the same name already exists. Use overwrite=true to replace it.");
        }

        // Delete the existing file if it exists and overwrite flag is true
        if (existingBlob != null) {
            existingBlob.delete();
        }

        // Upload the file to Firebase Storage
        Blob uploadedBlob = StorageClient.getInstance().bucket()
                .create(userId + "/" + fileName, file.getBytes(), file.getContentType());

        // Save file metadata to Firestore
        saveFileMetadata(fileName, userId, uploadedBlob.getMediaLink());  // Save the media link of the uploaded file

        return uploadedBlob.getMediaLink();  // Return the file's media URL
    }

    // Helper method to save metadata of the file into Firestore
    private void saveFileMetadata(String fileName, String userId, String fileUrl) {
        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(fileName);
        metadata.setOwnerId(userId);
        metadata.setFileUrl(fileUrl);
        metadata.setUploadDate(String.valueOf(System.currentTimeMillis()));  // Save current time as upload date

        firestore.collection(FILE_METADATA_COLLECTION).document(userId + "-" + fileName).set(metadata);
    }

    // Method to retrieve file URL by userId and fileName from Firestore
    public Optional<String> getFileUrl(String userId, String fileName) throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> future = firestore.collection(FILE_METADATA_COLLECTION)
                .whereEqualTo("ownerId", userId)
                .whereEqualTo("fileName", fileName)
                .get();

        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.getDocuments()
                .stream()
                .findFirst()
                .map(document -> document.getString("fileUrl"));
    }

    // Method to fetch files by date range
    public List<FileMetadata> getFilesByDateRange(String userId, String startDate, String endDate) throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> future = firestore.collection(FILE_METADATA_COLLECTION)
                .whereEqualTo("ownerId", userId)
                .whereGreaterThanOrEqualTo("uploadDate", startDate)
                .whereLessThanOrEqualTo("uploadDate", endDate)
                .get();

        QuerySnapshot querySnapshot = future.get();

        return querySnapshot.getDocuments()
                .stream()
                .map(document -> document.toObject(FileMetadata.class))
                .collect(Collectors.toList());
    }
}
