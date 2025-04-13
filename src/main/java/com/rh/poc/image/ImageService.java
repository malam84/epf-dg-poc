package com.rh.poc.image;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final RemoteCache<String, byte[]> remoteCache;
    
    @Autowired
    private ImageRepository imageRepository;

    public ImageService(RemoteCacheManager remoteCacheManager) {
        // Create a cache for images using Infinispan (Data Grid)
        this.remoteCache = remoteCacheManager.getCache("images");
    }

    public void storeImage(MultipartFile file) throws IOException {
        byte[] imageBytes = file.getBytes();

        // Store image in DataGrid (Infinispan)
        String imageId = generateImageId();
       
        //write to cache
        long start = System.nanoTime();
        remoteCache.put(imageId, imageBytes);
        long end = System.nanoTime();
        long durationInMillis = (end - start) / 1_000_000;
        System.out.println("Insert into Data Grid Method took " + durationInMillis + " ms to execute.");
        //write to DB
        long startdb = System.nanoTime();
		saveImageToPostgres(imageId, imageBytes);
		 long enddb = System.nanoTime();
	     long durationInMillisdb = (enddb - startdb) / 1_000_000;
	     System.out.println("Insert into DataBase Method took " + durationInMillisdb + " ms to execute.");
    }

    private String generateImageId() {
        return "img-" + System.currentTimeMillis();
    }

    private void saveImageToPostgres(String imageId, byte[] imageBytes) {
    	 Image entity = new Image(imageId,imageBytes);
         imageRepository.save(entity);
    }
}

