package com.example.demo.common.service;

import com.example.demo.common.exception.business.file.InvalidFileException;
import com.example.demo.common.exception.business.user.UserNotFoundException;
import com.example.demo.common.exception.technical.StorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.file-storage.location:uploads}")
    private String storageLocation;

    @Value("${app.file-storage.allowed-types:image/jpeg,image/png,image/gif}")
    private List<String> allowedTypes;

    @Value("${app.file-storage.max-size:5242880}") // 5MB по умолчанию
    private long maxFileSize;

    @Value("${app.base-url}")
    private String baseUrl;

    private Path rootLocation;

    public String storeFile(MultipartFile file, String subdirectory){
        validateFile(file);
        try{
            Path targetDir = rootLocation.resolve(subdirectory).normalize();
            Files.createDirectories(targetDir);

            String fileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
            Path filePath = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return Paths.get(subdirectory, fileName).toString();
        }catch (IOException e){
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public Resource loadFileAsResource(String filePath) {
        try {
            Path file = rootLocation.resolve(filePath).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists()) {
                throw new FileNotFoundException("File not found: " + filePath);
            }

            return resource;
        } catch (MalformedURLException | FileNotFoundException e) {
            throw new StorageException("File not found: " + filePath, e);
        }
    }

    public void deleteFile(String filePath) {
        try {
            Path file = rootLocation.resolve(filePath).normalize();
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file: " + filePath, e);
        }
    }

    private void validateFile(MultipartFile file) {
        if(file.isEmpty()){
            throw new InvalidFileException("File is empty");
        }

        if((file.getSize() > maxFileSize)){
            throw new InvalidFileException("File is too large");
        }

        String contentType = file.getContentType();
        if(contentType == null || !allowedTypes.contains(contentType)){
            throw new InvalidFileException("File type not allowed. Allowed types: " + String.join(", ", allowedTypes));
        }
    }

    private String getFileExtension(String filename) {

        if(filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }

    public String buildFileUrl(String filePath) {
        return baseUrl + "/api/files/" + filePath;
    }
}
