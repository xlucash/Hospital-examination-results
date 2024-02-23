package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.models.Image;
import me.lukaszpisarczyk.Hospital.repositories.ImageRepository;
import me.lukaszpisarczyk.Hospital.services.implementation.ImageServiceImpl;
import me.lukaszpisarczyk.Hospital.utils.ImageUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.zip.DataFormatException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageUtils imageUtils;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    void testUploadImage() throws IOException {
        byte[] imageData = "test image data".getBytes();
        MultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", imageData);
        Image savedImage = new Image(1L, "test.jpg", "image/jpeg", ImageUtils.compressImage(imageData));
        when(imageRepository.save(any(Image.class))).thenReturn(savedImage);

        Image result = imageService.uploadImage(imageFile);

        assertThat(result.getName()).isEqualTo(imageFile.getOriginalFilename());
        assertThat(result.getType()).isEqualTo(imageFile.getContentType());
        verify(imageRepository).save(any(Image.class));
    }

    @Test
    void testDownloadImage() throws IOException, DataFormatException {
        byte[] originalData = "test image data".getBytes();
        byte[] compressedData = ImageUtils.compressImage(originalData);
        Image dbImage = new Image(1L, "test.jpg", "image/jpeg", compressedData);
        when(imageRepository.findById(1L)).thenReturn(Optional.of(dbImage));

        byte[] result = imageService.downloadImage(1L);

        assertThat(result).isEqualTo(originalData);
    }

    @Test
    void testDownloadImage_imageNotFound() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imageService.downloadImage(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Image not found with id: 1");
    }

    @Test
    void testGetBase64Image() throws IOException, DataFormatException {
        byte[] originalData = "test image data".getBytes();
        byte[] compressedData = ImageUtils.compressImage(originalData);
        Image dbImage = new Image(1L, "test.jpg", "image/jpeg", compressedData);
        when(imageRepository.findById(1L)).thenReturn(Optional.of(dbImage));

        String base64Result = imageService.getBase64Image(1L);

        assertThat(base64Result).isEqualTo(Base64.getEncoder().encodeToString(originalData));
    }

    @Test
    void testGetBase64Image_imageNotFound() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imageService.getBase64Image(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Image not found with id: 1");
    }
}