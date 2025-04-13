package com.rh.poc.image;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final RemoteCache<String, byte[]> remoteCache;

    public ImageService(RemoteCacheManager remoteCacheManager) {
        // Create a cache for images using Infinispan (Data Grid)
        this.remoteCache = remoteCacheManager.getCache("images");
    }

    public void storeImage(MultipartFile file) throws IOException {
        byte[] imageBytes = file.getBytes();

        // Store image in DataGrid (Infinispan)
        String imageId = generateImageId();
        System.out.println("Cache name >>"+remoteCache);
        System.out.println("generated image ID >>"+imageId);
        System.out.println("generated image bytes >>"+imageBytes);
        remoteCache.put(imageId, imageBytes);

        // Asynchronously store the image to PostgreSQL (write-behind) - For example, save the image in a separate DB thread
        new Thread(() -> saveImageToPostgres(imageId, imageBytes)).start();
    }

    private String generateImageId() {
        return "img-" + System.currentTimeMillis();
    }

    private void saveImageToPostgres(String imageId, byte[] imageBytes) {
        // Code to save the image bytes into PostgreSQL as a BLOB (binary large object)
        // Example using Spring Data JPA to save the image to PostgreSQL
        // Your repository should handle this operation.
    }
}

