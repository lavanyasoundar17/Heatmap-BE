package com.example.BE_Heatmap.model;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "file_metadata")
public class FileMetadata {

    private String fileName;
    private String ownerId;
    private String fileUrl;
    private String uploadDate;
    private String uploadTime;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
    public String getUploadTime() {
        return uploadTime;
    }
    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
}
