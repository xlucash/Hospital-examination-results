package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    Image uploadImage(MultipartFile imageFile) throws IOException;
    byte[] downloadImage(String imageName);
}
