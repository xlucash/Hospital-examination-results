package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.dto.ExaminationRequestDto;
import me.lukaszpisarczyk.Hospital.dto.ExaminationResultDto;
import me.lukaszpisarczyk.Hospital.enums.ExaminationType;
import me.lukaszpisarczyk.Hospital.exceptions.ExaminationResultNotFoundException;
import me.lukaszpisarczyk.Hospital.mapper.ExaminationResultMapper;
import me.lukaszpisarczyk.Hospital.models.ExaminationResult;
import me.lukaszpisarczyk.Hospital.models.Image;
import me.lukaszpisarczyk.Hospital.models.User;
import me.lukaszpisarczyk.Hospital.repositories.ExaminationResultRepository;
import me.lukaszpisarczyk.Hospital.services.implementation.ExaminationResultServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExaminationResultServiceTest {

    @InjectMocks
    private ExaminationResultServiceImpl examinationResultService;

    @Mock
    private ImageService imageService;

    @Mock
    private UserService userService;

    @Mock
    private ExaminationResultRepository examinationResultRepository;

    @Mock
    private TemplateEngine templateEngine;

    private ExaminationResultMapper examinationResultMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        examinationResultMapper = new ExaminationResultMapper();
    }

    @Test
    public void testSaveExaminationResult() throws IOException {
        ExaminationRequestDto examinationRequestDto = new ExaminationRequestDto();
        examinationRequestDto.setType(ExaminationType.XRAY);
        examinationRequestDto.setTitle("X-Ray Examination");
        examinationRequestDto.setDescription("This is an X-Ray examination");
        examinationRequestDto.setPesel("1234567890");

        User doctor = new User();
        User patient = new User();

        List<MultipartFile> images = new ArrayList<>();
        images.add(new MockMultipartFile("image1", new byte[0]));
        images.add(new MockMultipartFile("image2", new byte[0]));

        List<Image> savedImages = new ArrayList<>();
        savedImages.add(new Image());
        savedImages.add(new Image());

        when(userService.retrieveUserFromToken()).thenReturn(doctor);
        when(userService.findUserByPesel(examinationRequestDto.getPesel())).thenReturn(patient);
        when(imageService.uploadImage(any(MockMultipartFile.class))).thenReturn(new Image());
        when(examinationResultRepository.save(any(ExaminationResult.class))).thenReturn(new ExaminationResult());

        ExaminationResult savedExaminationResult = examinationResultService.saveExaminationResult(examinationRequestDto, images);

        verify(userService, times(1)).retrieveUserFromToken();
        verify(userService, times(1)).findUserByPesel(examinationRequestDto.getPesel());
        verify(imageService, times(2)).uploadImage(any(MockMultipartFile.class));
        verify(examinationResultRepository, times(1)).save(any(ExaminationResult.class));
        assertNotNull(savedExaminationResult);
    }

    @Test
    public void testGetExaminationResult() {
        Long examinationResultId = 1L;
        User patient = new User();
        patient.setId(1L);
        User doctor = new User();
        doctor.setId(2L);
        ExaminationResult examinationResult = new ExaminationResult();
        examinationResult.setPatient(patient);
        examinationResult.setDoctor(doctor);
        examinationResult.setId(examinationResultId);

        List<Image> savedImages = new ArrayList<>();
        savedImages.add(new Image());
        savedImages.add(new Image());

        examinationResult.setImages(savedImages);

        when(examinationResultRepository.findById(examinationResultId)).thenReturn(java.util.Optional.of(examinationResult));

        ExaminationResultDto resultDto = examinationResultService.getExaminationResult(examinationResultId);

        verify(examinationResultRepository, times(1)).findById(examinationResultId);
        assertNotNull(resultDto);
        assertEquals(examinationResultId, resultDto.getId());
    }

    @Test
    public void testGetExaminationResult_NotFound() {
        Long examinationResultId = 1L;

        when(examinationResultRepository.findById(examinationResultId)).thenReturn(java.util.Optional.empty());

        assertThrows(ExaminationResultNotFoundException.class, () -> {
            examinationResultService.getExaminationResult(examinationResultId);
        });

        verify(examinationResultRepository, times(1)).findById(examinationResultId);
    }
}