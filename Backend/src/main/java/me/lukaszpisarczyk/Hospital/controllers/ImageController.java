package me.lukaszpisarczyk.Hospital.controllers;

import lombok.RequiredArgsConstructor;
import me.lukaszpisarczyk.Hospital.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile imageFile) throws IOException {
        String uploadImage = imageService.uploadImage(imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadImage);
    }

    @PostMapping("/multiple")
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

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
        byte[] imageData = imageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(MediaType.IMAGE_PNG_VALUE))
                .body(imageData);
    }
}
