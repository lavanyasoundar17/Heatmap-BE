package com.example.BE_Heatmap.repository;
import com.example.BE_Heatmap.model.FileMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepository extends MongoRepository<FileMetadata, String> {
    FileMetadata findByFileNameAndUploadDateAndUploadTime(String fileName, String uploadDate, String uploadTime);
}

