package com.app.service;

import com.app.entiry.FileUploadResponse;
import com.app.exceptions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    
    public FileUploadResponse uploadImage(String path, MultipartFile file) {
        
        // Validate file type
        if (!isValidFileType(file.getContentType())) {
            throw new InvalidFileTypeException("Invalid file type");
        }
        
        // Create a unique file name
        String fileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
        
        String filePath = path + File.separator + fileName;
        
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        
        try {
            Files.copy(file.getInputStream(), Paths.get(filePath));
        } catch (IOException e) {
            throw new FileUploadException("Error uploading file", e);
        }
        
        return new FileUploadResponse(fileName, filePath);
    }
    
    private boolean isValidFileType(String contentType) {
        // Add allowed file types here
        return contentType.startsWith("image/");
    }
    
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            return fileName.substring(dotIndex);
        }
        return "";
    }
}




