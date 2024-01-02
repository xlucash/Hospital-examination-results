package me.lukaszpisarczyk.Hospital.services.implementation;

import jakarta.transaction.Transactional;
import me.lukaszpisarczyk.Hospital.models.Image;
import me.lukaszpisarczyk.Hospital.repositories.ImageRepository;
import me.lukaszpisarczyk.Hospital.services.ImageService;
import me.lukaszpisarczyk.Hospital.utils.ImageUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;
    @Override
    public Image uploadImage(MultipartFile imageFile) throws IOException {
        var imageToSave = Image.builder()
                .name(imageFile.getOriginalFilename())
                .type(imageFile.getContentType())
                .imageData(ImageUtils.compressImage(imageFile.getBytes()))
                .build();

        return imageRepository.save(imageToSave);
    }

    @Override
    @Transactional
    public byte[] downloadImage(String imageName) {
        Optional<Image> dbImage = imageRepository.findByName(imageName);
        return dbImage.map(image -> {
            try {
                return ImageUtils.decompressImage(image.getImageData());
            } catch (DataFormatException | IOException exception) {
                throw new ContextedRuntimeException("Error downloading an image", exception)
                        .addContextValue("Image ID",  image.getId())
                        .addContextValue("Image name", imageName);
            }
        }).orElse(null);
    }
}
