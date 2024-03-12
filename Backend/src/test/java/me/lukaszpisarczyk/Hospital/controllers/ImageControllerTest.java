package me.lukaszpisarczyk.Hospital.controllers;

import me.lukaszpisarczyk.Hospital.models.Image;
import me.lukaszpisarczyk.Hospital.repositories.ImageRepository;
import me.lukaszpisarczyk.Hospital.services.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageRepository imageRepository;

    @MockBean
    private ImageService imageService;

    @Test
    @WithMockUser(username = "doctor@example.com", password = "testdoctor123", roles = "DOCTOR")
    void shouldUploadImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "<<png data>>".getBytes()
        );

        mockMvc.perform(multipart("/api/image").file(file))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "doctor@example.com", password = "testdoctor123", roles = "DOCTOR")
    void shouldUploadMultipleImages() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile(
                "image",
                "firstTest.png",
                MediaType.IMAGE_PNG_VALUE,
                "<<png data>>".getBytes()
        );

        MockMultipartFile secondFile = new MockMultipartFile(
                "image",
                "secondTest.png",
                MediaType.IMAGE_PNG_VALUE,
                "<<png data>>".getBytes()
        );

        mockMvc.perform(multipart("/api/image/multiple")
                        .file(firstFile)
                        .file(secondFile))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "doctor@example.com", password = "testdoctor123", roles = "DOCTOR")
    void shouldDownloadImage() throws Exception {
        imageRepository.deleteAll();
        Image imageToSave = Image.builder()
                .name("testImage.png")
                .type(MediaType.IMAGE_PNG_VALUE)
                .imageData("<<png data>>".getBytes())
                .build();

        Image savedImage = imageRepository.save(imageToSave);

        mockMvc.perform(get("/api/image/" + savedImage.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(MediaType.IMAGE_PNG_VALUE)));
    }
}