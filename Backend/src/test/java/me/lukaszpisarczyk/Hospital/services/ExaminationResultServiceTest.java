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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Test
    public void testGetExaminationResultByPatient() {
        User patient = new User();
        patient.setId(1L);

        ExaminationType type = ExaminationType.XRAY;
        List<ExaminationResult> examinationResults = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            User doctor = new User();
            doctor.setId(2L + i);

            ExaminationResult examinationResult = new ExaminationResult();
            examinationResult.setId(1L + i);
            examinationResult.setPatient(patient);
            examinationResult.setDoctor(doctor);

            List<Image> images = List.of(new Image());
            examinationResult.setImages(images);

            examinationResults.add(examinationResult);
        }

        when(userService.retrieveUserFromToken()).thenReturn(patient);
        when(examinationResultRepository.findAllByPatientAndType(eq(patient), eq(type))).thenReturn(examinationResults);

        List<ExaminationResultDto> resultDtos = examinationResultService.getExaminationResultByPatient(type.name());

        assertNotNull(resultDtos);
        assertEquals(2, resultDtos.size());
        verify(userService, times(1)).retrieveUserFromToken();
        verify(examinationResultRepository, times(1)).findAllByPatientAndType(eq(patient), eq(type));
    }

    @Test
    public void testGetExaminationResultByDoctor() {
        User doctor = new User();
        doctor.setId(2L); // Assuming the doctor's ID is set to 2L

        ExaminationType type = ExaminationType.MRI;
        List<ExaminationResult> examinationResults = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User patient = new User();
            patient.setId(1L + i);

            ExaminationResult examinationResult = new ExaminationResult();
            examinationResult.setPatient(patient);
            examinationResult.setDoctor(doctor);
            examinationResult.setId(1L + i);

            List<Image> savedImages = new ArrayList<>();
            savedImages.add(new Image());
            savedImages.add(new Image());
            examinationResult.setImages(savedImages);

            examinationResults.add(examinationResult);
        }

        when(userService.retrieveUserFromToken()).thenReturn(doctor);
        when(examinationResultRepository.findAllByDoctorAndType(eq(doctor), eq(type))).thenReturn(examinationResults);

        List<ExaminationResultDto> resultDtos = examinationResultService.getExaminationResultByDoctor(type.name());

        verify(userService, times(1)).retrieveUserFromToken();
        verify(examinationResultRepository, times(1)).findAllByDoctorAndType(eq(doctor), eq(type));
        assertNotNull(resultDtos);
        assertEquals(5, resultDtos.size());
    }

    @Test
    public void testProcessExaminationResultPdf() {
        Long examinationResultId = 1L;
        ExaminationResult examinationResult = new ExaminationResult();
        examinationResult.setId(examinationResultId);
        examinationResult.setImages(List.of(new Image()));

        when(examinationResultRepository.findById(examinationResultId)).thenReturn(java.util.Optional.of(examinationResult));
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html></html>");

        byte[] pdfContent = examinationResultService.processExaminationResultPdf(examinationResultId);

        verify(examinationResultRepository, times(1)).findById(examinationResultId);
        assertNotNull(pdfContent);
    }

    @Test
    public void testGetAllExaminationResult() {
        User patient = new User();
        patient.setId(1L);

        List<ExaminationResult> examinationResults = new ArrayList<>();
        ExaminationResult examinationResult1 = new ExaminationResult();
        examinationResult1.setId(1L);
        examinationResult1.setPatient(patient);
        examinationResult1.setDoctor(new User());
        examinationResult1.setImages(List.of(new Image()));

        ExaminationResult examinationResult2 = new ExaminationResult();
        examinationResult2.setId(2L);
        examinationResult2.setPatient(patient);
        examinationResult2.setDoctor(new User());
        examinationResult2.setImages(List.of(new Image()));

        examinationResults.add(examinationResult1);
        examinationResults.add(examinationResult2);

        when(userService.retrieveUserFromToken()).thenReturn(patient);
        when(examinationResultRepository.findAllByPatient(patient)).thenReturn(examinationResults);

        List<ExaminationResultDto> resultDtos = examinationResultService.getAllExaminationResult();

        assertNotNull(resultDtos);
        assertEquals(2, resultDtos.size());
        verify(examinationResultRepository, times(1)).findAllByPatient(patient);
    }

    @Test
    public void testSaveExaminationResultWithIOException() throws IOException {
        ExaminationRequestDto examinationRequestDto = new ExaminationRequestDto();
        List<MultipartFile> images = List.of(new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes()));

        when(userService.retrieveUserFromToken()).thenReturn(new User()); // Mocked User

        when(imageService.uploadImage(any(MultipartFile.class))).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () -> {
            examinationResultService.saveExaminationResult(examinationRequestDto, images);
        });
    }
}