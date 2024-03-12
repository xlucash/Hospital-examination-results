package me.lukaszpisarczyk.Hospital.controllers;

import me.lukaszpisarczyk.Hospital.models.Image;
import me.lukaszpisarczyk.Hospital.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('USER')")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile imageFile) throws IOException {
        Image uploadImage = imageService.uploadImage(imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadImage);
    }

    @PostMapping("/multiple")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('USER')")
    public ResponseEntity<?> uploadMultipleImages(@RequestParam("image") List<MultipartFile> images) throws IOException {
        List<ResponseEntity<?>> list = new ArrayList<>();
        for (MultipartFile image : images) {
            ResponseEntity<?> responseEntity = uploadImage(image);
            list.add(responseEntity);
        }
        return list.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        ResponseEntity::ok
                        )
                );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('USER')")
    public ResponseEntity<?> downloadImage(@PathVariable("id") Long imageId) {
        byte[] imageData = imageService.downloadImage(imageId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(MediaType.IMAGE_PNG_VALUE))
                .body(imageData);
    }
}
