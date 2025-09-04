package com.app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.exceptions.*;

import com.app.service.ImageService;

@RestController
@RequestMapping("/api/image")
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	@Value("${spring.servlet.multipart.location}")
	String imagePath;
	
	@PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            imageService.uploadImage(imagePath, file);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (InvalidFileTypeException e) {
            return ResponseEntity.badRequest().body("Invalid file type");
        } catch (FileUploadException e) {
            return ResponseEntity.internalServerError().body("Error uploading file");
        }
    }
	
	@GetMapping("/{fileName}")
    public ResponseEntity<byte[]> fetchImage(@PathVariable String fileName) {
        try {
            File file = new File(imagePath + File.separator + fileName);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            byte[] imageData = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
