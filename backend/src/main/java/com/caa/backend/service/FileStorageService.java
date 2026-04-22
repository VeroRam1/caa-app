package com.caa.backend.service;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {
    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    /**
     * Saves a profile photo to disk.
     * Generates a unique filename to avoid collisions.
     *
     * @param file the uploaded image file
     * @return the relative URL path to access the file (e.g. /uploads/profiles/abc123.jpg)
     * @throws IOException if the file cannot be saved
     */
    public String saveProfilePhoto(MultipartFile file) throws IOException {
        // Validate file type — only images allowed
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException(
                    "Solo se permiten archivos de imagen (JPG, PNG, GIF, WebP)"
            );
        }

        // Extract extension from original filename
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Generate unique filename
        String filename = UUID.randomUUID().toString() + extension;

        // Create profiles subdirectory if it doesn't exist
        Path profilesDir = Paths.get(uploadDir, "profiles");
        Files.createDirectories(profilesDir);

        // Save file to disk
        Path targetPath = profilesDir.resolve(filename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        log.info("Profile photo saved: {}", targetPath);

        // Return the URL path the frontend will use to display the image
        return "/uploads/profiles/" + filename;
    }

    /**
     * Deletes a previously saved profile photo from disk.
     * Called when a profile photo is replaced or the profile is deleted.
     *
     * @param photoUrl the URL path of the photo to delete (e.g. /uploads/profiles/abc123.jpg)
     */
    public void deleteProfilePhoto(String photoUrl) {
        if (photoUrl == null || !photoUrl.startsWith("/uploads/")) return;

        try {
            // Convert URL path back to filesystem path
            String relativePath = photoUrl.substring(1); // remove leading /
            Path filePath = Paths.get(relativePath);
            Files.deleteIfExists(filePath);
            log.info("Profile photo deleted: {}", filePath);
        } catch (IOException e) {
            log.warn("Could not delete profile photo: {}", photoUrl, e);
        }
    }

}
