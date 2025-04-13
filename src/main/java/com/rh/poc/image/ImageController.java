package com.rh.poc.image;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
        	System.out.println("Method Mapped >>"+file);
            imageService.storeImage(file);
            return ResponseEntity.ok().body("Image uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading image.");
        }
    }
}
