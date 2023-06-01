package com.babydev.app.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private String fileStorageLocation = "D:/babydev/files/cv";

    public String storeFile(MultipartFile file) throws Exception {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        try {
            Path targetLocation = Paths.get(fileStorageLocation).resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
        	e.printStackTrace();
            throw new Exception("Failed to store file " + fileName, e);
        }
    }

    public byte[] getFileContent(String fileName) throws Exception {
        try {
            Path filePath = Paths.get(fileStorageLocation).resolve(fileName);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new Exception("Failed to read file " + fileName, e);
        }
    }

    public void deleteFile(String fileName) throws Exception {
        try {
            // Delete the file from the target location
            Path targetLocation = Paths.get(fileStorageLocation).resolve(fileName);
            Files.deleteIfExists(targetLocation);
        } catch (IOException e) {
            throw new Exception("Failed to delete file " + fileName, e);
        }
    }
}

